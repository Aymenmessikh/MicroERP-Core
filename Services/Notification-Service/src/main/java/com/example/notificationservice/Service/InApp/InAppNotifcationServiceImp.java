package com.example.notificationservice.Service.InApp;

import com.example.notificationservice.Entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InAppNotifcationServiceImp implements InAppNotifcationService{

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void inAppPush(String destination, Notification notification) {
            messagingTemplate.convertAndSend(destination, notification);
    }
}
