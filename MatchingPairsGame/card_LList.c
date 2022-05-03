#include <stdlib.h>
#include <stdbool.h>
//use the "forward declaration" technique so Card can be used here

#include "card_LList.h"

/*
CMPT 125 Assignment 4 
Author: Param Tully
Academic honesty statement: I hereby confirm that this is my own work and I have
not violated any of SFU’s Code of Academic Integrity and Good Conduct (S10.01).
Description: 
*/

//a function that creates a new Card_LList, which is an empty linked list.
Card_LList* createCard_LList() {

  //initialize LList
  Card_LList* llist= malloc(sizeof(Card_LList));
  if(llist == NULL) exit(0);

  //make members null
  llist->head = NULL;
  llist->tail = NULL;

  return llist;
}

//a function that removes all the nodes from the list
// by freeing them one by one. The result is an empty linked list.
void clearCard_LList(Card_LList* theList) {

  //check for valid arguments
  if(theList == NULL) return;

  //free nodes
  Card_Node* current = theList->head;
  Card_Node* prev;

  while(current) {
    prev = current;
    current = current->next;
    free(prev);
  }

  //set both head and tail to null
  theList->head = NULL;
  theList->tail = NULL;
}

//a function that checks if the list is empty.
// If it is empty, return true; otherwise return false.
bool isEmptyCard_LList(Card_LList* theList) {
  
  //check for valid arguments
  if(theList == NULL) exit(0);

  //check if empty
  if(theList->head == NULL && theList->tail == NULL) return true;
  else return false;

}

//a function that inserts the card as a part of a node
// (the node itself doesn't store the card, but the address of the card).
// This method encapsulates the inner workings of the linked list and
// there is no need to duplicate the card.
void insertFrontCard_LList(Card_LList* theList, Card* theCard) {

  //validate arguments
  if(theList == NULL || theCard == NULL) return;

  //initialize card node
  Card_Node* node = malloc(sizeof(Card_Node));
  if(node == NULL) exit(0);
  node->card = theCard;

  //prepend node to empty list
  if(theList->head == NULL) {
    node->next = NULL;
    theList->head = node;
    theList->tail = node;
  }
  //prepend node to not empty list
  else {
    node->next = theList->head;
    theList->head = node;
  }
}

//a function that inserts the card as a part of a node
// (the node itself doesn't store the card, but the address of the card).
// This method encapsulates the inner workings of the linked list and
// there is no need to duplicate the card.
void insertEndCard_LList(Card_LList* theList, Card* theCard) {

  //validate arguments
  if(theList == NULL) return;
  else if(theCard == NULL) return;

  //initialize card node
  Card_Node* node = malloc(sizeof(Card_Node));
  if(node == NULL) exit(0);
  node->card = theCard;
  node->next = NULL;

  //append node to empty list
  if(theList->head == NULL) {
    theList->head = node;
    theList->tail = node;
  }
  //append node to not empty list
  else{
    theList->tail->next = node;
    theList->tail = node;
  }
}

//a function that returns the address of the card stored in the node.
// This method encapsulates the inner workings of the linked list and
// there is no need to duplicate the card.
// Freeing of the node memory is done here.
Card* removeFrontCard_LList(Card_LList* theList) {

  //check validity of arguments
  if(theList == NULL) exit(0);

  //if empty return null
  if(theList->head == NULL) return NULL;

  Card_Node* node = theList->head;
  Card* card;

  //condition if 1 item in llist
  if(theList->head == theList->tail) {

    //get card then free node
    card = node->card;
    free(node);

    //set head and tail to null
    theList->head = NULL;
    theList->tail = NULL;
  }

  else {
    //set new head
    theList->head = node->next;

    //free node then return card
    card = node->card;
    free(node);
  }
  return card;
}


//a function that returns the address of the card stored in the node.
// This method encapsulates the inner workings of the linked list and
// there is no need to duplicate the card.
// Freeing of the node memory is done here.
Card* removeEndCard_LList(Card_LList* theList) {

  //check validity of arguments
  if(theList == NULL) exit(0);

  //if empty return null
  if(theList->head == NULL) return NULL;


  Card_Node* node = theList->head;
  Card* card;

  //remove from 1 node list
  if(theList->head == theList->tail) {
    //get card then release node
    card = node->card;
    free(node);

    //set head and tail to null
    theList->head = NULL;
    theList->tail = NULL;
  }

  //remove from list with more than 1 node
  else {
    //find second last node
    while(node->next != theList->tail) node = node->next;

    //reassign tail
    theList->tail = node;
    

    //return card and free old tail
    node = node->next;
    card = node->card;
    free(node);
    
    //make tail next null
    theList->tail->next = NULL;
  }

  return card;
}