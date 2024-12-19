package com.lysero.cartes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;

    public void registerDeck(DeckRegistrationRequest request) {
        Deck deck = Deck.builder()
                .playerId(request.playerId())
                .name(request.name())
                .cardIdList(request.cardIdList())
                .build();

        deckRepository.save(deck);
    }
}
