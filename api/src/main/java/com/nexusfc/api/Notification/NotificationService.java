package com.nexusfc.api.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyUser(String userId, Object payload) {
        logger.info("Enviando Notificacao para {}", userId);
        messagingTemplate
                .convertAndSendToUser(
                        userId,
                        "/queue/notifications",
                        payload);
    }

}
