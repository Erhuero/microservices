package com.lysero.cartes;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Deck {
    private UUID deckId;
    private UUID playerId;
    private String name;
    private List<UUID> cardIdList;
}
