package com.brianrook.crypto.exception;

import com.brianrook.crypto.model.Deck;

public class InvalidKeyStreamValueException extends Throwable {
    Deck currentDeck;
    public InvalidKeyStreamValueException(String message, Deck deckIn) {
        super(message);
        currentDeck = deckIn;
    }

    public Deck getCurrentDeck() {
        return currentDeck;
    }
}
