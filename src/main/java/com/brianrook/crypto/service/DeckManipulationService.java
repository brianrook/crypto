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
         int jokerPosition = deckIn.getCards().lastIndexOf(Deck.JOKER_A);

         //if the joker is not at the end of the deck, move down one
         if (jokerPosition!=Deck.DECK_SIZE-1){
             deckIn.getCards().remove(jokerPosition);
             deckIn.getCards().add(jokerPosition+1, Deck.JOKER_A);
         }
         //joker in last position, move between front 2 cards
         else{
             deckIn.getCards().remove(jokerPosition);
             deckIn.getCards().add(1, Deck.JOKER_A);
         }
         return deckIn;
     }
}
