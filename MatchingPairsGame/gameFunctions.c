#include <stdbool.h> //to be able to use bool
#include <stdlib.h>
#include <stdio.h>
#include "card_LList.h"
#include "gameObjects.h"

/*
CMPT 125 Assignment 4 
Author: Param Tully
Academic honesty statement: I hereby confirm that this is my own work and I have
not violated any of SFU’s Code of Academic Integrity and Good Conduct (S10.01).
Description:
*/

#define NUM_CARDS_IN_SUIT 13 //constant number of cards per suit

//a function that adds the card to the player's winning pile by calling
// the appropriate function from card_LList and update the cards won.
// Also marks the card as taken ('0').
void addCardToPlayer(Player* thePlayer, Card* theCard) {

  //validate arguments
  if(thePlayer == NULL) return;
  else if (theCard == NULL) return;

  //mark card as taken
  theCard->value = '0';

  //append card to win pile
  insertEndCard_LList(&thePlayer->winPile, theCard);

}

//a function that checks if the user choice is valid:
// if any of the choices are invalid, report that and return false.
bool checkPlayerInput(Deck* theDeck, Player* thePlayer, char row, char col) {

  //validate arguments
  if(theDeck == NULL) exit(0);
  else if(thePlayer == NULL) exit(0);

  
  //checks if card has been chosen
  if((theDeck->cards[(row - '0') * NUM_CARDS_IN_SUIT + (col - 'a')]).value == '0') {
    printf("Error: The card you picked is already taken.\n");
    return false;
  }
  //check for invalid choice
  else if('a' > col || 'm' < col) {
    printf("Error: The card you picked has invalid index(es).\n");
    return false;
  }
  else if('0' > row || '3' < row) {
    printf("Error: The card you picked has invalid index(es).\n");
    return false;
  }
  //checks if card has been chosen
  
  else return true;
}

//a function that checks if there is a match:
// if the two choices are the same, report it and return false.
// if there is a match, return true; otherwise, return false.
bool checkForMatch(Deck* theDeck, Player* thePlayer,
                      char r1, char c1, char r2, char c2) {
                  
                        // validate arguments
                        if(theDeck == NULL) exit(0);
                        else if(thePlayer == NULL) exit(0);

                        //check if both choices same
                        if(r1 == r2 && c1 == c2) {
                          printf("Error: Both cards the same.\n");
                          return false;
                        }
                        
                        //get both cards chosen
                        Card* card1 = theDeck->cards + ((r1 - '0') * NUM_CARDS_IN_SUIT + (c1 - 'a'));
    Card* card2 = theDeck->cards + ((r2 - '0') * NUM_CARDS_IN_SUIT + (c2 - 'a'));
                        //check for match
                        if(card1->value == card2->value) return true;
                        else return false;
                      }

//a function that checks if the game has a winner
// (all cards in the deck is taken)
bool checkForWinner(const Deck* theDeck) {

  //validate arguments
  if(theDeck == NULL) exit(0);

  //look for non 0 card
  for(unsigned int i = 0; i < NUM_OF_CARDS_IN_DECK; i++) {
    Card card = theDeck->cards[i];
    if(card.value != '0') return false;
  }
  return true;
}