package com.brianrook.crypto.service;

import com.brianrook.crypto.model.Deck;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeckManipulationService {

     public static String writeDeck(Deck deckIn){
         String cardStr = String.join(" ",deckIn.getCards());
         log.debug(cardStr);
         return cardStr;
     }

     public static Deck firstManipulation(Deck deckIn){
         return moveJoker(deckIn, Deck.JOKER_A);
     }
    public static Deck secondManipulation(Deck deckIn){
        Deck firstMove = moveJoker(deckIn, Deck.JOKER_B);
        return moveJoker(firstMove, Deck.JOKER_B);
    }

     public static Deck moveJoker(Deck deckIn, String joker){
         int jokerPosition = deckIn.getCards().lastIndexOf(joker);

         //if the joker is not at the end of the deck, move down one
         if (jokerPosition!=Deck.DECK_SIZE-1){
             deckIn.getCards().remove(jokerPosition);
             deckIn.getCards().add(jokerPosition+1, joker);
         }
         //joker in last position, move between front 2 cards
         else{
             deckIn.getCards().remove(jokerPosition);
             deckIn.getCards().add(1, joker);
         }
         return deckIn;
     }
}
