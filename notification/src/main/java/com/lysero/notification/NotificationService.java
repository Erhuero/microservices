package com.lysero.notification;

import lombok.AllArgsConstructor;
import notification.NotificationRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest notificationRequest) {
        notificationRepository.save(
                Notification.builder()
                        .toPlayerId(notificationRequest.toPlayerId())
                        .toPlayerEmail(notificationRequest.toPlayerName())
                        .sender("Realm Factions")
                        .message(notificationRequest.message())
                        .sentAt(LocalDateTime.now())
                        .build()
        );
    }
}