package notification;

public record NotificationRequest(
        java.util.UUID toPlayerId,
        String toPlayerName,
        String message
) {
}
