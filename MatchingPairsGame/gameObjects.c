#include <stdbool.h> //so bool can be used
#include <stdio.h> //so printf can be used
#include <stdlib.h> //so NULL and srand+rand can be used
#include "card_LList.h"

/*
CMPT 125 Assignment 4
Author: Param Tully
Academic honesty statement: I hereby confirm that this is my own work and I have
not violated any of SFU’s Code of Academic Integrity and Good Conduct (S10.01).
Description: ...
*/

#include "gameObjects.h"
#define NUM_SUITS 4 
#define NUM_CARDS_IN_SUIT 13 //constant number of cards per suit

//a function that initializes all the fields of a Deck.
// For each suit, the values of the cards are
// ‘A’, ‘2’, ‘3’, …, ‘9’, ,'T', ‘J’, ‘Q’, ‘K’.
void initializeDeck(Deck* theDeck, char* brandName) {

  //check if arguments are valid
  if(theDeck == NULL) return;
  else if (brandName == NULL) return;
  
  //assign brand 
  theDeck->brand = brandName;

  //set cards in deck
  char values[] = "A23456789TJQK";

  for(unsigned int i = 0; i < NUM_SUITS; i++) {
    for(unsigned int j = 0; j < NUM_CARDS_IN_SUIT; j++) {
      theDeck->cards[i*NUM_CARDS_IN_SUIT + j].suit = i;
      theDeck->cards[i*NUM_CARDS_IN_SUIT + j].value = values[j];
    }
  }
}

//a function that shuffles the deck
void shuffleDeck(Deck* theDeck) {
  //condition to check if deck is valid
  if(theDeck == NULL) return;

  //condition to check if deck has enough cards to shuffle
  if(NUM_OF_CARDS_IN_DECK > 1) {

    //shuffle cards
    for(unsigned int i = 0; i < NUM_OF_CARDS_IN_DECK - 1; i++) {

      //find random index to swap
      int j = i + rand() / (RAND_MAX / (NUM_OF_CARDS_IN_DECK - i) + 1);

      //swap cards
      struct Card t = theDeck->cards[j];
      theDeck->cards[j] = theDeck->cards[i];
      theDeck->cards[i] = t; 
    }
  }
}

//a function that prints the content of a Deck.
//accepts a second bool parameter:
//if true, print face up, if false, print face down.
//if the card is won by a player, leave it blank.
void printDeck(const Deck* theDeck, bool faceUp) {

  //check if deck is valid
  if(theDeck == NULL) return;

  //line up columns with cards
  printf("   ");

  //print indeces of columns
  char cols = 'a';
  while(cols != 'a' + NUM_CARDS_IN_SUIT) {
    printf("%c", cols);
    cols++;

    if(cols != 'a' + NUM_CARDS_IN_SUIT) printf("  ");
    else printf("\n");
  }

  for(unsigned int i = 0; i < NUM_SUITS; i++) {

    //print cards index then cards
    printf("%d ", i);

    //print cards
    for(unsigned int j = 0; j < NUM_CARDS_IN_SUIT; j++) {

      //define current card
      struct Card current_card = theDeck->cards[i * 13 + j];

      //print face down if not chosen
      if(!faceUp) {
        if(current_card.value == '0') printf("  ");
        else printf("?\u218C");
      }
      //print face up if not chosen
      else if(current_card.value == '0') {
        printf("  ");
      }
      else {
        printf("%c", current_card.value);

        //print current card suit
        if(current_card.suit == Spades) printf("\u2660");
        else if(current_card.suit == Hearts) printf("\u2661");
        else if (current_card.suit == Clubs) printf("\u2663");
        else printf("\u2662");
      }

      //put spaces after every card not at end of line
      //or newline at end of line
      if(j != NUM_CARDS_IN_SUIT - 1) printf(" ");
      else printf("\n");
    }
  }

}


//a function that initializes all the fields of a Player.
void initializePlayer(Player* thePlayer, char* theName) {

  //check for valid arguments
  if(thePlayer == NULL) return;
  else if(theName == NULL) return;

  //initialize members
  thePlayer->name = theName;
  thePlayer->cardsWon = 0;
  thePlayer->winPile = *createCard_LList();
}

//a function clears the winning pile of the player by calling the necessary
// function on a Card_LList.
void clearPlayer(Player* thePlayer) {
  
  //check if arguments valid
  if(thePlayer == NULL) return;

  //reset members
  thePlayer->name = NULL;
  thePlayer->cardsWon = 0;
  clearCard_LList(&thePlayer->winPile);
}