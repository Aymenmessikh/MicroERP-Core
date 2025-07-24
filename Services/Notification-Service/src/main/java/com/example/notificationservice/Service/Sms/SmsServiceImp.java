package com.example.notificationservice.Service.Sms;

import com.example.notificationservice.Entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class SmsServiceImp implements SmsService{
    @Override
    public void sendSms(Notification notification) {

    }

    // @Override
//    public void sendSms(Notification notification) {
//        Message message = Message.creator(
//                        new PhoneNumber(notification.get),
//                        new PhoneNumber(fromPhoneNumber),
//                        messageContent)
//                .create();
//
//        System.out.println("SMS envoyé: " + message.getSid());
//    }
}
