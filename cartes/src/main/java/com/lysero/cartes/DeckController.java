package com.lysero.cartes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/deck")
@AllArgsConstructor
public class DeckController {

    private final DeckService deckService;

    @PostMapping
    public void registerDeck(@RequestBody DeckRegistrationRequest deckRegistrationRequest) {
        log.info("new deck registration {}", deckRegistrationRequest);
    deckService.registerDeck(deckRegistrationRequest);
    }
}
