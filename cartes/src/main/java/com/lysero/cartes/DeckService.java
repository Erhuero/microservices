package com.lysero.cartes;

import com.lysero.clients.fraud.FraudCheckResponse;
import com.lysero.clients.fraud.FraudClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;
    private final FraudClient fraudClient;

    public void registerDeck(DeckRegistrationRequest request) {
        Deck deck = Deck.builder()
                .playerId(request.playerId())
                .name(request.name())
                .cardIdList(request.cardIdList())
                .build();

        deckRepository.saveAndFlush(deck);

        FraudCheckResponse fraudCheckResponse =
                fraudClient.isFraudster(deck.getDeckId());

        System.out.println("Response from FraudClient: " + fraudCheckResponse.isFraudster());

        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("fraudster");
        }
    }
}
