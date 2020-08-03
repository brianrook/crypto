package com.brianrook.crypto.controller;

import com.brianrook.crypto.controller.model.CryptoRequest;
import com.brianrook.crypto.service.CryptoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CryptoController {

    @PostMapping(path = "/encode",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public String encodeMessage(@RequestBody CryptoRequest encodeRequest){
        log.debug("received encode request: {}", encodeRequest);
        String encodedMessage = CryptoService.encode(
                encodeRequest.getMessage(),
                encodeRequest.getDeckSeed());

        return encodedMessage;
    }
    @PostMapping(path = "/decode",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public String decodeMessage(@RequestBody CryptoRequest decodeRequest){
        log.debug("received decode request: {}", decodeRequest);
        String decodedMessage = CryptoService.decode(
                decodeRequest.getMessage(),
                decodeRequest.getDeckSeed());

        return decodedMessage;
    }
}
