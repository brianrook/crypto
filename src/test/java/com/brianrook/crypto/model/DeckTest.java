package com.brianrook.crypto.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckTest {

    @Test
    public void testDeckConstructor(){
        Deck myDeck = new Deck();

        assertEquals("1", myDeck.getCards().get(0));
        assertEquals(Deck.JOKER_B, myDeck.getCards().get(Deck.DECK_SIZE-1));
        assertEquals(Deck.JOKER_A, myDeck.getCards().get(Deck.DECK_SIZE-2));
        assertEquals("52", myDeck.getCards().get(Deck.DECK_SIZE-3));
    }
}
