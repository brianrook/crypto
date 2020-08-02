package com.brianrook.crypto.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Deck {

    public final static String JOKER_A = "jokerA";
    public final static String JOKER_B = "jokerB";

    public final static int NUMBER_OF_CARDS=52;

    private ArrayList<String> cards;

    public Deck(){
        cards = new ArrayList();
        for (int i=1; i<=NUMBER_OF_CARDS; i++){
            cards.add(""+i);
        }
        cards.add(JOKER_A);
        cards.add(JOKER_B);
    }

    public int getDeckSize(){
        return cards.size()-1;
    }
}
