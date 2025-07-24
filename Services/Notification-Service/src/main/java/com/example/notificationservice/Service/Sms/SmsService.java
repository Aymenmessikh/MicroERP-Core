package com.example.notificationservice.Service.Sms;

import com.example.notificationservice.Entity.Notification;

public interface SmsService {
    void sendSms(Notification notification);
}
