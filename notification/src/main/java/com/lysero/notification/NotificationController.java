package com.lysero.notification;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.lysero.clients.notification.NotificationRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("api/v1/notification")
@AllArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public void sendNotification(@RequestBody NotificationRequest notificationRequest) {
        log.info("Nouvelle notification... {}", notificationRequest);
        notificationService.send(notificationRequest);
    }
}