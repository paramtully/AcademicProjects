/*
CMPT 125 Assignment 4 Game
Author: Param Tully
Academic honesty statement: I hereby confirm that this is my own work and I have
not violated any of SFU’s Code of Academic Integrity and Good Conduct (S10.01).
Description: This is the source code for the game. In implements the main
function that is the driver of the program. In also includes a clear function
that attemps to "push" the face up print up beyond the screen.
*/

#include <stdio.h>
#include <stdlib.h> //for the use of system and srand and rand
#include "gameObjects.h"
#include "gameFunctions.h"

#define MAX_CHAR_NUMBER 16 //each input should have at most 15 characters
#define NUM_CARDS_IN_SUIT 13 // number of cards per suit

//a helper function that clears the screen, works for both unix and Windows,
//though behaviour might be different.
//reference: https://stackoverflow.com/questions/2347770/
//            how-do-you-clear-the-console-screen-in-c
void clear() {
  #if defined(__linux__) || defined(__unix__) || defined(__APPLE__)
    system("printf '\33[H\33[2J'");
  #endif

  #if defined(_WIN32) || defined(_WIN64)
    system("cls");
  #endif
}

int main() {

  int counter = 0;
  //set the random seed to 0, it'll generate the same sequence
  //normally it is srand(time(NULL)) so the seed is different in every run
  //using 0 makes it deterministic so it is easier to mark
  //do not change it
  srand(0);

  //variables to store user input
  char userInput1[MAX_CHAR_NUMBER];
  char userInput2[MAX_CHAR_NUMBER];
  //int whereInDeck = 0; //handy for indexing which card based on user input

  //set up the players
  Player player1, player2;
  initializePlayer(&player1, "Player 1");
  initializePlayer(&player2, "Player 2");
  Player* currentPlayer = &player1; //this pointer remembers who's turn it is

  //set up the deck: initalize & shuffle
  Deck myDeck;
  initializeDeck(&myDeck, "Bicycle");
  shuffleDeck(&myDeck);

  printDeck(&myDeck, true); //print the shuffled deck, face up
  printf("\n");
  //clear(); //clear the screen

  /*
  Implement this part for the game to work.
  It is essentially a do-while loop that repeatedly print the deck,
  ask for user inputs, and do some checking. Ends if someone wins.
  */

  //number to determine whos turn it is 
  do {
    /////////////////////
    // 1 Round
    ////////////////////
    //Step 1: print the shuffled deck, face down
    printDeck(&myDeck, false);


    //Step 2: print who's turn it is by showing the player's name

    if(currentPlayer == &player1) {
      printf("Player 1's turn.\n");
    }
    else{
      printf("Player 2's turn.\n");
    }

    //Step 3.1 get first input from player,
    // keep asking until the input is valid (hint: use a do-while loop)
    // you can assume that the format is correct (a digit<space>a letter)
    do {
      counter++;
      //take user input
      printf("Pick the first card you want to pick (e.g., 0 a) then press enter: ");

      fgets(userInput1, MAX_CHAR_NUMBER, stdin);

      //keep looping while input is invalid
    } while(!checkPlayerInput(&myDeck, currentPlayer, userInput1[0], userInput1[2]));


    //Step 3.2: get second input from player,
    // keep asking until the input is valid (hint: use a do-while loop)
    // you can assume that the format is correct (a digit<space>a letter)

    do {
      counter++;
      //take user input
      printf("Pick the second card you want to pick (e.g., 0 b) then press enter: ");
      fgets(userInput2, MAX_CHAR_NUMBER, stdin);
      

    } while(!checkPlayerInput(&myDeck, currentPlayer, userInput2[0], userInput2[2]));


    //Step 4: print the 2 cards the player picks
    
    //find cards
    Card* card1 = myDeck.cards + ((userInput1[0] - '0') * NUM_CARDS_IN_SUIT + (userInput1[2] - 'a'));
    Card* card2 = myDeck.cards + ((userInput2[0] - '0') * NUM_CARDS_IN_SUIT + (userInput2[2] - 'a'));

    //print first card
    printf("First card picked: %c", card1->value);
    if(card1->suit == Spades) printf("\u2660\n");
    else if(card1->suit == Hearts) printf("\u2661\n");
    else if (card1->suit == Clubs) printf("\u2663\n");
    else printf("\u2662\n");

    //print second card
    printf("Second card picked: %c", card2->value);
    if(card2->suit == Spades) printf("\u2660\n");
    else if(card2->suit == Hearts) printf("\u2661\n");
    else if (card2->suit == Clubs) printf("\u2663\n");
    else printf("\u2662\n");

    //Step 5: call the checkForMatch function to see if the player has picked
    // a matching pair. If so, print the corresponding message and add the cards
    // to the player's winning pile (Player.winPile).
    // Keep the current player as a match leads to an extra round.
    // Otherwise, print the corresponding message and switch player.

    if(checkForMatch(&myDeck, currentPlayer, userInput1[0], userInput1[2], userInput2[0], userInput2[2])){

      //print won round message
      if(currentPlayer == &player1) {
        printf("Player 1 has found a match! Earns an extra turn!\n");
      }
      else {
        printf("Player 2 has found a match! Earns an extra turn!\n");
      }

      //add cards
      addCardToPlayer(currentPlayer, card1);
      addCardToPlayer(currentPlayer, card2);
    }
    //change player 
    else if(currentPlayer == &player1) {
      printf("Player1 has not found a match.\n");
      currentPlayer = &player2;
    }
    else {
      printf("Player1 has not found a match.\n");
      currentPlayer = &player1;
    }
    printf("\n %d \n", counter);

  } while (!checkForWinner(&myDeck));

  //Step 6: find out who won the game and announce the winner
  int p1cards = 0;
  int p2cards = 0;

  //counter first players won cards
  Card_Node* node = player1.winPile.head;
  while(node != NULL) {
    p1cards++;
    node = node->next;
  }

  //count second players won cards
  node = player2.winPile.head;
  while(node != NULL) {
    p2cards++;
    node = node->next;
  }

  //print how many cards each player has won
  printf("Player 1 has won %d\n", p1cards);
  printf("Player 2 has won %d\n", p2cards);

  //print winner
  if(p1cards > p2cards) {
    printf("Player 1 has won!\n");
  }
  else if(p1cards < p2cards) {
    printf("Player 2 has won!\n");
  }
  else {
    printf("It's a tie!");
  }

  //Step 7: clean up player's winning piles
  clearPlayer(&player1);
  clearPlayer(&player2);

  return 0;
}