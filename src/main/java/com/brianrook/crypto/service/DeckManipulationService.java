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

}
