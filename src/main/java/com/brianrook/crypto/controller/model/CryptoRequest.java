package com.brianrook.crypto.controller.model;

import lombok.Value;

import java.util.List;

@Value
public class CryptoRequest {

    private String message;
    private List<String> deckSeed;
}
