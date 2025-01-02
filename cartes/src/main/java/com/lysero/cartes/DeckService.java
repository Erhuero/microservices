package com.lysero.cartes;

import com.lysero.amqp.RabbitMQMessageProducer;
import com.lysero.clients.fraud.FraudCheckResponse;
import com.lysero.clients.fraud.FraudClient;
import lombok.AllArgsConstructor;
import com.lysero.clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

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

        NotificationRequest notificationRequest = new NotificationRequest(
                deck.getPlayerId(),
                deck.getName(), String.format("Hi %s, your deck is ready!", deck.getName())
        );
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"

        );
    }
}
