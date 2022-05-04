#include <stdlib.h>
#include <stdio.h>

#include "uthread.h"
#include "uthread_mutex_cond.h"
#include "uthread_sem.h"
#include "threadpool.h"

#ifdef VERBOSE
#define VERBOSE_PRINT(S, ...) printf (S, ##__VA_ARGS__)
#else
#define VERBOSE_PRINT(S, ...) ((void) 0) // do nothing
#endif

enum type {
  WORKER = 0, TASK = 1, ARG = 0
};

struct Node {
  struct Node *next;
  struct Node *prev;
  void *val;
} typedef Node;

struct LinkedList {
  struct Node *head;
  struct Node *tail;
} typedef LinkedList;

struct Monitor {
  int unlocked;
  int num_workers;
  int remaining_tasks;
  uthread_sem_t start_working;
  uthread_sem_t mx;
  uthread_sem_t tasks_done;
  uthread_sem_t task_pop;
  uthread_sem_t worker_pop;
} monitor;

struct tpool {
  LinkedList *tasks;
  LinkedList *workers;
  LinkedList *args;
};

void initMonitor(int num_workers) {
  monitor.unlocked = 0;
  monitor.num_workers = num_workers;
  monitor.remaining_tasks = 0;
  monitor.start_working = uthread_sem_create(0);
  monitor.tasks_done = uthread_sem_create(0);
  monitor.mx = uthread_sem_create(1);
  monitor.task_pop   = uthread_sem_create(0);
  monitor.worker_pop  = uthread_sem_create(0);
}

// REQUIRES: user must ensure atomicity
int isEmpty(LinkedList *l) {
  return !l->head;
}

void enqueue(LinkedList *l, void *val) {
  Node *node = malloc(sizeof(Node));
  if (isEmpty(l)) {
    l->head = node;
    l->tail = node;
    l->head->prev = NULL;
  } else if (l->head == l->tail) {
    l->tail = node;
    l->head->next = l->tail;
    l->tail->prev = l->head;
  }
  else {
    Node *temp = l->tail;
    l->tail = node;
    temp->next = l->tail;
    l->tail->prev = temp;
  }
  l->tail->next = NULL;
  l->tail->val = val;
}

void *dequeue(LinkedList *l, int isTasks) {
  void *val;
  if (isEmpty(l)) exit(EXIT_FAILURE);
  else if (l->head == l->tail) {
    val = l->head->val;
    free(l->head);
    l->head = NULL;
    l->tail = NULL;
    if(isTasks) uthread_sem_signal(monitor.tasks_done);
  }
  else {
    Node *nextTail = l->tail->prev;
    val = l->tail->val;
    nextTail->next = NULL;
    free(l->tail);
    l->tail = nextTail;
  }
  return val;
}

/* Function executed by each pool worker thread. This function is
 * responsible for running individual tasks. The function continues
 * running as long as either the pool is not yet joined, or there are
 * unstarted tasks to run. If there are no tasks to run, and the pool
 * has not yet been joined, the worker thread must be blocked.
 * 
 * Parameter: param: The pool associated to the thread.
 * Returns: nothing.
 */
static void *worker_thread(void *param) {
  tpool_t pool = param;
  uthread_sem_wait(monitor.start_working);                        // waits for task to be available or all tasks to
  VERBOSE_PRINT("worker: started\n");                             // be completed before starting worker
  while (1) {
    VERBOSE_PRINT("worker: checking if all tasks completed\n");
    uthread_sem_wait(monitor.mx);
      if (monitor.remaining_tasks == 0) {
        VERBOSE_PRINT("worker: no tasks found\n");
        while (monitor.unlocked++ < monitor.num_workers)          // unlocks all workers still waiting to start
          uthread_sem_signal(monitor.start_working);
          
        uthread_sem_signal(monitor.mx);
        break;
      }
    uthread_sem_signal(monitor.mx);
    
    VERBOSE_PRINT("worker: waiting for task to become available\n");
    uthread_sem_wait(monitor.task_pop);
    VERBOSE_PRINT("worker: starting task\n");
    uthread_sem_wait(monitor.mx);
      void (*fun)(tpool_t, void*) = dequeue(pool->tasks, TASK);
      void *arg = dequeue(pool->args, ARG);
      monitor.remaining_tasks--;
    uthread_sem_signal(monitor.mx);
    fun(pool, arg);
  }
  VERBOSE_PRINT("Thread done\n");
  return NULL;
}

/* Creates (allocates) and initializes a new thread pool. Also creates
 * `num_threads` worker threads associated to the pool, so that
 * `num_threads` tasks can run in parallel at any given time.
 *
 * Parameter: num_threads: Number of worker threads to be created.
 * Returns: a pointer to the new thread pool object.
 */
tpool_t tpool_create(unsigned int num_threads) {
  tpool_t pool = malloc(sizeof(struct tpool));
  pool->workers = malloc(sizeof(LinkedList));
  pool->tasks = malloc(sizeof(LinkedList));
  pool->args = malloc(sizeof(LinkedList));

  initMonitor(num_threads);
    for (int i = 0; i < num_threads; i++) {
      uthread_sem_wait(monitor.mx);
      enqueue(pool->workers, uthread_create(worker_thread, pool));
      uthread_sem_signal(monitor.mx);
    }
  VERBOSE_PRINT("tpool_create: done create\n");

  return pool;
}

/* Queues a new task, to be executed by one of the worker threads
 * associated to the pool. The task is represented by function `fun`,
 * which receives the pool and a generic pointer as parameters. If any
 * of the worker threads is available, `fun` is started immediately by
 * one of the worker threads. If all of the worker threads are busy,
 * `fun` is scheduled to be executed when a worker thread becomes
 * available. Tasks are retrieved by individual worker threads in the
 * order in which they are scheduled, though due to the nature of
 * concurrency they may not start exactly in the same order. This
 * function returns immediately, and does not wait for `fun` to
 * complete.
 *
 * Parameters: pool: the pool that is expected to run the task.
 *             fun: the function that should be executed.
 *             arg: the argument to be passed to fun.
 */
void tpool_schedule_task(tpool_t pool, void (*fun)(tpool_t, void *),
                         void *arg) {
  uthread_sem_wait(monitor.mx);
    monitor.remaining_tasks++;
    enqueue(pool->tasks, fun);
    enqueue(pool->args, arg);
    uthread_sem_signal(monitor.task_pop);
    monitor.unlocked++;
  uthread_sem_signal(monitor.mx);
  uthread_sem_signal(monitor.start_working);
}

/* Blocks until the thread pool has no more scheduled tasks; then,
 * joins all worker threads, and frees the pool and all related
 * resources. Once this function returns, the pool cannot be used
 * anymore.
 *
 * Parameters: pool: the pool to be joined.
 */
void tpool_join(tpool_t pool) {
  if (monitor.remaining_tasks != 0) 
    uthread_sem_wait(monitor.tasks_done);
  else VERBOSE_PRINT("tpool_join: did not wait\n");
  
  while (!isEmpty(pool->workers)) {
    uthread_join(dequeue(pool->workers, WORKER), NULL);
  }

  free(pool->tasks);
  free(pool->workers);
  free(pool);
}
