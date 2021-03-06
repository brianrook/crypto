package com.brianrook.crypto.service.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Deck {

    public final static String JOKER_A = "jokerA";
    public final static String JOKER_B = "jokerB";

    public final static int DEFAULT_NUMBER_OF_CARDS=26;


    private List<String> cards;

    public Deck(){
        cards = new ArrayList();
        for (int i=1; i<=DEFAULT_NUMBER_OF_CARDS; i++){
            cards.add(""+i);
        }
        cards.add(JOKER_A);
        cards.add(JOKER_B);
    }
    public Deck(int deckSize){
        cards = new ArrayList();
        for (int i=1; i<=deckSize; i++){
            cards.add(""+i);
        }
        cards.add(JOKER_A);
        cards.add(JOKER_B);
    }

    public Deck(List<String> deckSeed) {
        this.cards = deckSeed;
    }

    public int getDeckSize(){
        return cards.size()-1;
    }
    public int getNonJokerCards(){
        return cards.size()-2;
    }



}
