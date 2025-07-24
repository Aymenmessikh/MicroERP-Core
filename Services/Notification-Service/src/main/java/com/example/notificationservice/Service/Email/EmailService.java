package com.example.notificationservice.Service.Email;

import com.example.notificationservice.Entity.Notification;

public interface EmailService {
    void sendEmail(Notification notification);
}
