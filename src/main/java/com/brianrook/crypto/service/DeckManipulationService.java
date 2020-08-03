package com.brianrook.crypto.service;

import com.brianrook.crypto.exception.InvalidKeyStreamValueException;
import com.brianrook.crypto.model.Deck;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j

/*
Solitaire algorithm from
https://en.wikipedia.org/wiki/Solitaire_(cipher)
 */
public class DeckManipulationService {

     public static String writeDeck(Deck deckIn){
         String cardStr = String.join(" ",deckIn.getCards());
         return cardStr;
     }

     public static Deck firstManipulation(Deck deckIn){
         log.info("executing firstManipulation:\n{}", writeDeck(deckIn));
         return moveJoker(deckIn, Deck.JOKER_A);
     }
    public static Deck secondManipulation(Deck deckIn){
        log.info("executing secondManipulation:\n{}", writeDeck(deckIn));
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
         log.info("executing triplecut:\n{}", writeDeck(deckIn));

         //joker a and b are not always in order, get first and second joker positions
         int jokerAPosition = deckIn.getCards().indexOf(Deck.JOKER_A);
         int jokerBPosition = deckIn.getCards().indexOf(Deck.JOKER_B);

         int firstJoker = jokerAPosition<jokerBPosition ? jokerAPosition : jokerBPosition;
         int secondJoker = jokerAPosition>jokerBPosition?jokerAPosition : jokerBPosition;


         List<String> firstCut = deckIn.getCards().subList(0, firstJoker);
         List<String> secondCut = deckIn.getCards().subList(firstJoker, secondJoker+1);
         List<String> thirdCut = new ArrayList<>();
         if (secondJoker+1 <= deckIn.getDeckSize())
         {
             log.debug("joker at end of deck");
             thirdCut= deckIn.getCards().subList(secondJoker+1, deckIn.getDeckSize()+1);
         }


         ArrayList<String> recombine = new ArrayList<>();
         recombine.addAll(thirdCut);
         recombine.addAll(secondCut);
         recombine.addAll(firstCut);

         deckIn.setCards(recombine);
         log.info("finished triplecut:\n{}", writeDeck(deckIn));
         return deckIn;
     }

    public static Deck countCut(Deck deckIn){
        log.info("executing countCut:\n{}", writeDeck(deckIn));
         String lastCard = deckIn.getCards().get(deckIn.getDeckSize());
         log.info("last card: {}", lastCard);
         int cutSize = 0;
         //if joker in last position
        if (lastCard==Deck.JOKER_A || lastCard==Deck.JOKER_B){
            cutSize = deckIn.getNonJokerCards()+1;
        }else{
            cutSize = Integer.valueOf(lastCard);
        }
        log.info("cutSize: {}, deckSize: {}", cutSize, deckIn.getDeckSize());

        List<String> cutCards = deckIn.getCards().subList(0,cutSize);
        List<String> remainingCards = deckIn.getCards().subList(cutSize, deckIn.getDeckSize());

        ArrayList<String> recombine = new ArrayList<>();
        recombine.addAll(remainingCards);
        recombine.addAll(cutCards);
        recombine.add(lastCard);

        deckIn.setCards(recombine);
        return deckIn;
    }

    public static int getStreamValue(Deck deckIn) throws InvalidKeyStreamValueException {
         log.info("getStreamValue: {}", writeDeck(deckIn));
         String topCard = deckIn.getCards().get(0);

         int topCardValue = 0;
         if (topCard.equals(Deck.JOKER_A) || topCard.equals(Deck.JOKER_B)){
             log.info("top card was a joker");
             topCardValue = deckIn.getNonJokerCards()+1;
         }else{
             topCardValue = Integer.valueOf(topCard);
         }
         log.info("top card value: {}", topCardValue);

         String keyStreamValue = deckIn.getCards().get(topCardValue);
         log.info("keystream value: {}", keyStreamValue);
         if (keyStreamValue.equals(Deck.JOKER_A) || keyStreamValue.equals(Deck.JOKER_B)){
             log.warn("key stream outcome was a joker");
             throw new InvalidKeyStreamValueException("outcome was a joker", deckIn);
         }

         return Integer.valueOf(keyStreamValue);
    }

    public static int executeKeyStream(Deck deckIn){
         try{
             Deck firstManipulation = DeckManipulationService.firstManipulation(deckIn);
             Deck secondManipulation = DeckManipulationService.secondManipulation(firstManipulation);
             Deck tripleCut = DeckManipulationService.tripleCut(secondManipulation);
             Deck countCut = DeckManipulationService.countCut(tripleCut);
             int keyStreamValue = DeckManipulationService.getStreamValue(countCut);

             return keyStreamValue;
         }catch (InvalidKeyStreamValueException e){
             //execute the keystream algorithm again
             return executeKeyStream(e.getCurrentDeck());
         }
    }

    public static String encode(String messageToEncode){
         Deck myDeck = new Deck();

         return encode(messageToEncode, myDeck);
    }

    public static char encodeCharacter(char characterToEncode, Deck myDeck){
         int myCharValue = convertAlphaToNumber(characterToEncode);
         int keyStreamValue = executeKeyStream(myDeck);
         int mod = myCharValue + keyStreamValue % myDeck.getNonJokerCards();
         char encodedChar = convertNumberToAlpha(mod);
         log.info("encoded character: {}", encodedChar);
         return encodedChar;
    }

    public static int convertAlphaToNumber(Character c){
         int pos = c -'A' +1;
         return pos;
    }
    public static char convertNumberToAlpha(int i){
         i=i+64;
        return (char)i;
    }

    public static char decodeCharacter(char characterToDecode, Deck myDeck){
        int myCharValue = convertAlphaToNumber(characterToDecode);
        log.info("my encoded char: {}", myCharValue);
        int keyStreamValue = executeKeyStream(myDeck);
        log.info("keystream value returned: {}", keyStreamValue);
        int diff = myCharValue - keyStreamValue;
        if (diff<0)
        {
            diff=diff+myDeck.getNonJokerCards();
        }
        log.info("diff value: {}", diff);
        char encodedChar = convertNumberToAlpha(diff);
        log.info("decoded character: {}", encodedChar);
        return encodedChar;
    }

    public static String decode(String messageToDecode){
        List<Integer> valueList = messageToDecode.chars().mapToObj(i -> (char)i)
                .map(c -> convertAlphaToNumber(c))
                .collect(Collectors.toList());

        Deck myDeck = new Deck();

        List<Character> encodedChars = valueList.stream()
                .map(i -> convertNumberToAlpha(i))
                .map(c -> decodeCharacter(c, myDeck))
                .collect(Collectors.toList());

        String decodedMessage = encodedChars.stream().map(String::valueOf).collect(Collectors.joining());
        log.info("encoded message : {}", decodedMessage);
        return decodedMessage;
    }

    public static String encode(String messageToEncode, List<String> deckSeed) {
         Deck myDeck = new Deck(deckSeed);
         return encode(messageToEncode, myDeck);
    }
    public static String encode(String messageToEncode, Deck myDeck){
        String cleanedMessage = messageToEncode.toUpperCase().replaceAll("[^A-Z]","");
        log.debug("message to encode: {}", cleanedMessage);

        List<Integer> valueList = cleanedMessage.chars().mapToObj(i -> (char)i)
                .map(c -> convertAlphaToNumber(c))
                .collect(Collectors.toList());

        List<Character> encodedChars = valueList.stream()
                .map(i -> convertNumberToAlpha(i))
                .map(c -> encodeCharacter(c, myDeck))
                .collect(Collectors.toList());

        String encodedMessage = encodedChars.stream().map(String::valueOf).collect(Collectors.joining());
        log.info("encoded message : {}", encodedMessage);
        return encodedMessage;
    }

    public static String decode(String message, List<String> deckSeed) {
         Deck myDeck = new Deck(deckSeed);
         return decode(message, myDeck);
    }
    public static String decode(String messageToDecode, Deck myDeck){
        List<Integer> valueList = messageToDecode.chars().mapToObj(i -> (char)i)
                .map(c -> convertAlphaToNumber(c))
                .collect(Collectors.toList());

        List<Character> encodedChars = valueList.stream()
                .map(i -> convertNumberToAlpha(i))
                .map(c -> decodeCharacter(c, myDeck))
                .collect(Collectors.toList());

        String decodedMessage = encodedChars.stream().map(String::valueOf).collect(Collectors.joining());
        log.info("encoded message : {}", decodedMessage);
        return decodedMessage;
    }
}
