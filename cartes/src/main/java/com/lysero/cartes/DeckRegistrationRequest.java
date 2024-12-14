package com.lysero.cartes;

import java.util.List;
import java.util.UUID;

public record DeckRegistrationRequest(UUID playerId, String name, List<UUID>cardIdList) {
}
