package com.brianrook.crypto.controller;

import com.brianrook.crypto.controller.model.CryptoRequest;
import com.brianrook.crypto.service.DeckManipulationService;
import jdk.jfr.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CryptoController {

    @PostMapping(path = "/encode",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public String encodeMessage(@RequestBody CryptoRequest encodeRequest){
        log.debug("received encode request: {}", encodeRequest);
        String encodedMessage = DeckManipulationService.encode(
                encodeRequest.getMessage(),
                encodeRequest.getDeckSeed());

        return encodedMessage;
    }
    @PostMapping(path = "/decode",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public String decodeMessage(@RequestBody CryptoRequest decodeRequest){
        log.debug("received decode request: {}", decodeRequest);
        String decodedMessage = DeckManipulationService.decode(
                decodeRequest.getMessage(),
                decodeRequest.getDeckSeed());

        return decodedMessage;
    }
}
