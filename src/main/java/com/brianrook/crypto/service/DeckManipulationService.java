package com.brianrook.crypto.service;

import com.brianrook.crypto.exception.InvalidKeyStreamValueException;
import com.brianrook.crypto.model.Deck;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j

/*
Solitaire algorithm from
https://en.wikipedia.org/wiki/Solitaire_(cipher)
 */
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
         if (jokerPosition!=deckIn.getDeckSize()){
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

     public static Deck tripleCut(Deck deckIn){

         //joker a and b are not always in order, get first and second joker positions
         int jokerAPosition = deckIn.getCards().indexOf(Deck.JOKER_A);
         int jokerBPosition = deckIn.getCards().indexOf(Deck.JOKER_B);

         int firstJoker = jokerAPosition<jokerBPosition ? jokerAPosition : jokerBPosition;
         int secondJoker = jokerAPosition>jokerBPosition?jokerAPosition : jokerBPosition;


         List<String> firstCut = deckIn.getCards().subList(0, firstJoker);
         List<String> secondCut = deckIn.getCards().subList(firstJoker, secondJoker+1);
         List<String> thirdCut = new ArrayList<>();
         if (secondJoker+1 < deckIn.getDeckSize())
         {
             thirdCut= deckIn.getCards().subList(secondJoker+1, deckIn.getDeckSize());
         }


         ArrayList<String> recombine = new ArrayList<>();
         recombine.addAll(thirdCut);
         recombine.addAll(secondCut);
         recombine.addAll(firstCut);

         deckIn.setCards(recombine);
         return deckIn;
     }

    public static Deck countCut(Deck deckIn){
         String lastCard = deckIn.getCards().get(deckIn.getDeckSize());
         int cutSize = 0;
         //if joker in last position
        if (lastCard==Deck.JOKER_A || lastCard==Deck.JOKER_B){
            cutSize = deckIn.getNonJokerCards()+1;
        }else{
            cutSize = Integer.valueOf(lastCard);
        }

        List<String> cutCards = deckIn.getCards().subList(0,cutSize);
        List<String> remainingCards = deckIn.getCards().subList(cutSize, deckIn.getDeckSize());

        ArrayList<String> recombine = new ArrayList<>();
        recombine.addAll(remainingCards);
        recombine.addAll(cutCards);
        recombine.add(lastCard);

        deckIn.setCards(recombine);
        return deckIn;
    }

    public static String getStreamValue(Deck deckIn) throws InvalidKeyStreamValueException {
         String topCard = deckIn.getCards().get(0);

         int topCardValue = 0;
         if (topCard==Deck.JOKER_A || topCard==Deck.JOKER_B){
             topCardValue = deckIn.getNonJokerCards()+1;
         }else{
             topCardValue = Integer.valueOf(topCard);
         }

         String keyStreamValue = deckIn.getCards().get(topCardValue);
         if (keyStreamValue==Deck.JOKER_A || topCard==Deck.JOKER_B){
             throw new InvalidKeyStreamValueException("outcome was a joker", deckIn);
         }

         return keyStreamValue;
    }

    public static String executeKeyStream(Deck deckIn){
         try{
             Deck firstManipulation = DeckManipulationService.firstManipulation(deckIn);
             Deck secondManipulation = DeckManipulationService.secondManipulation(firstManipulation);
             Deck tripleCut = DeckManipulationService.tripleCut(secondManipulation);
             Deck countCut = DeckManipulationService.countCut(tripleCut);
             String keyStreamValue = DeckManipulationService.getStreamValue(countCut);

             return keyStreamValue;
         }catch (InvalidKeyStreamValueException e){
             //execute the keystream algorithm again
             return executeKeyStream(e.getCurrentDeck());
         }
    }
}
