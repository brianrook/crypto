package com.brianrook.crypto.service;

import com.brianrook.crypto.service.model.Deck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CryptoService {

    public static String encode(String messageToEncode){
        Deck myDeck = new Deck();

        return encode(messageToEncode, myDeck);
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

    public static String decode(String message) {
        Deck myDeck = new Deck();
        return decode(message, myDeck);
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

    public static char encodeCharacter(char characterToEncode, Deck myDeck){
        int myCharValue = convertAlphaToNumber(characterToEncode);
        int keyStreamValue = DeckManipulationService.executeKeyStream(myDeck);
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
        int keyStreamValue = DeckManipulationService.executeKeyStream(myDeck);
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

}
