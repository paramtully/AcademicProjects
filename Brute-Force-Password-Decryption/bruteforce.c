#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <crypt.h>

#include "uthread_sem.h"
#include "uthread.h"
#include "threadpool.h"

#ifdef VERBOSE
#define VERBOSE_PRINT(S, ...) printf (S, ##__VA_ARGS__)
#else
#define VERBOSE_PRINT(S, ...) ((void) 0) // do nothing
#endif

#define MAX_SIZE 5
#define SIZE 5000

int found = 0;
int data_index;
const char *hash_string;
struct crypt_data data[SIZE];

// EFFECTS: tries password attempt against given hash
//          if the password is correct, marks process as completed and prints correct password
//          otherwise queues next password attempts to be processed
void testHashString(tpool_t pool, void *vpass) {
  char *password = vpass;
  char *output = crypt_r(password, hash_string, data + ((data_index++)%SIZE));

  VERBOSE_PRINT("PASSWORD: %s    OUTPUT: %s      Same?: %s\n", 
                password, output, strcmp(password, output) == 0? "TRUE!!!!!!" : "F");

  if (strcmp(output, hash_string) == 0) {
    found = 1;
    printf("%s\n", password);
  }
  else if (!found) pushNextPasswords(pool, password);
   free(password);
}

// EFFECTS: given prefix of password, queues all password attempts 
//          one digit longer than prefix to be processed
void pushNextPasswords(tpool_t pool, char *prefix) {
  int size = strlen(prefix) + 2;
  if (size - 1 > MAX_SIZE) return;
  for (int i = 0; i < 10; i++) {
    char *newpass = malloc(size);
    strcpy(newpass, prefix);
    newpass[size - 2] = '0' + i;
    newpass[size - 1] = 0;
    tpool_schedule_task(pool, testHashString, (void *)newpass);
  }
}

// EFFECTS: initiates brute force password decryption
void solve(tpool_t pool) {
  char *password = malloc(sizeof(char));
  password = "";
  tpool_schedule_task(pool, testHashString, (void *)password);
}

int main(int argc, char *argv[]) {

  tpool_t pool;
  int num_threads, num_tasks;

  if (argc != 3) {
    fprintf(stderr, "Usage: %s NUM_THREADS NUM_TASKS\n", argv[0]);
    return -1;
  }
  
  num_threads = strtol(argv[1], NULL, 10);
  hash_string = argv[2];

  uthread_init(8);
  pool = tpool_create(num_threads);

  solve(pool);
  tpool_join(pool);
  
  return 0;
}
