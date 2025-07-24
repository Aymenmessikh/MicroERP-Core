package com.example.notificationservice.Service.InApp;

import com.example.notificationservice.Entity.Notification;
public interface InAppNotifcationService {
    void inAppPush(String destination,Notification notification);
}
