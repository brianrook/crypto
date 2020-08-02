package com.brianrook.crypto.service;

import com.brianrook.crypto.model.Deck;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        assertEquals(Deck.JOKER_A, returnDeck.getCards().get(myDeck.getDeckSize()));
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

    @Test
    public void testSecondManipulation(){
        //given
        Deck myDeck = new Deck();

        //when
        Deck returnDeck = DeckManipulationService.secondManipulation(myDeck);

        //joker should be in third position
        assertEquals(Deck.JOKER_B, returnDeck.getCards().get(2));
    }

    @Test
    public void testTripleCut(){
        //given
        Deck myDeck = new Deck();

        //when
        Deck returnDeck = DeckManipulationService.tripleCut(myDeck);

        assertEquals(Deck.JOKER_A, returnDeck.getCards().get(0));
        assertEquals(Deck.JOKER_B, returnDeck.getCards().get(1));
        assertEquals("1", returnDeck.getCards().get(2));
        assertEquals("52", returnDeck.getCards().get(myDeck.getDeckSize()));
    }
    @Test
    public void testTripleCut1(){
        //given
        Deck myDeck = new Deck();
        //example from wiki
        ArrayList<String> cards = Lists.newArrayList("1", "4", "7","10","13","16","19","22","25",
                "3","6",Deck.JOKER_B,"9","12","15","18","21","24","2",Deck.JOKER_A,"5","8","11","14","17","20","23","26");
        myDeck.setCards(cards);

        //when
        Deck returnDeck = DeckManipulationService.tripleCut(myDeck);

        assertEquals(Deck.JOKER_B, returnDeck.getCards().get(7));
        assertEquals(Deck.JOKER_A, returnDeck.getCards().get(15));
        assertEquals("5", returnDeck.getCards().get(0));
        assertEquals("6", returnDeck.getCards().get(returnDeck.getDeckSize()));
    }

    @Test
    public void testCountCut(){
        //given
        Deck myDeck = new Deck();
        //example from wiki
        ArrayList<String> cards = Lists.newArrayList("5","8","11","14","17","20","23","26",Deck.JOKER_B,"9","12","15","18","21","24","2",Deck.JOKER_A,"1","4","7","10","13","16","19","22","25","3","6");
        myDeck.setCards(cards);

        //when
        Deck returnDeck = DeckManipulationService.countCut(myDeck);

        String expected = "23 26 jokerB 9 12 15 18 21 24 2 jokerA 1 4 7 10 13 16 19 22 25 3 5 8 11 14 17 20 6";
        assertEquals(expected, DeckManipulationService.writeDeck(returnDeck));
    }
}
