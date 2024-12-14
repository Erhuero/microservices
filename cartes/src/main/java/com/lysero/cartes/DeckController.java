package com.lysero.cartes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/deck")
public record DeckController(DeckService deckService) {

    @PostMapping
    public void registerDeck(@RequestBody DeckRegistrationRequest deckRegistrationRequest) {
        log.info("new customer regestration {}", deckRegistrationRequest);
    deckService.registerDeck(deckRegistrationRequest);
    }
}
