package com.brianrook.crypto.service;

import com.brianrook.crypto.model.Deck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckManipulationServiceTest {

    @Test
    public void testWriteDeck(){
        Deck myDeck = new Deck();

        assertEquals("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 jokerA jokerB",
                DeckManipulationService.writeDeck(myDeck));
    }

    @Test
    public void testFirstManipulationJokerANotAtEnd(){
        Deck myDeck = new Deck();

        Deck returnDeck = DeckManipulationService.firstManipulation(myDeck);

        //joker should be in last position
        assertEquals(Deck.JOKER_A, returnDeck.getCards().get(Deck.DECK_SIZE-1));
    }

    @Test
    public void testFirstManipulationJokerAAtEnd(){
        //given
        Deck myDeck = new Deck();

        //when
        Deck returnDeck = DeckManipulationService.firstManipulation(myDeck);
        returnDeck = DeckManipulationService.firstManipulation(myDeck);

        //joker should be in second position
        assertEquals(Deck.JOKER_A, returnDeck.getCards().get(1));
    }
}
