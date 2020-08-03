package com.brianrook.crypto.model;

import com.brianrook.crypto.service.model.Deck;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeckTest {

    @Test
    public void testDeckConstructor(){
        Deck myDeck = new Deck();

        assertEquals("1", myDeck.getCards().get(0));
        assertEquals(Deck.JOKER_B, myDeck.getCards().get(myDeck.getDeckSize()-1));
        assertEquals(Deck.JOKER_A, myDeck.getCards().get(myDeck.getDeckSize()-2));
        assertEquals("52", myDeck.getCards().get(myDeck.getDeckSize()-3));
    }
}
