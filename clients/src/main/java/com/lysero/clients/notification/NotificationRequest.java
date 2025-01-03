package com.lysero.clients.notification;

import java.util.UUID;

public record NotificationRequest(
        UUID toPlayerId,
        String toPlayerName,
        String message
) {
}
