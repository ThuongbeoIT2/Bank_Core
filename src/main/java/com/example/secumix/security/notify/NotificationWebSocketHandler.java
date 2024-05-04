package com.example.secumix.security.notify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationWebSocketHandler {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleNewNotificationEvent(NewNotificationEvent event) {
        Notify notification = event.getNotification();
        sendNotificationToUser(notification);
    }

    private void sendNotificationToUser(Notify notification) {
        messagingTemplate.convertAndSendToUser(
                notification.getUser().getUsername(), // Định danh người dùng làm điểm đến
                "/queue/notifications",
                notification);
    }
}
