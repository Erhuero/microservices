package com.lysero.cartes;

import org.springframework.stereotype.Service;

@Service
public record DeckService() {

    public void registerDeck(DeckRegistrationRequest request) {
        Deck deck = Deck.builder()
                .playerId(request.playerId())
                .name(request.name())
                .cardIdList(request.cardIdList())
                .build();


    }
}
