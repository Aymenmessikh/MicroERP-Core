//package com.example.adminservice.Kafka;
//
//import com.example.adminservice.Dto.Notification.MailMessage;
//import com.example.adminservice.Dto.Notification.SmsMessage;
//import com.example.adminservice.Services.EmailService;
//import com.example.adminservice.Services.SmsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@RequiredArgsConstructor
//@Component
//public class NotifyUserConsumer {
//    private final EmailService emailService;
//    private final SmsService smsService;
//
//    @KafkaListener(topics = "Notify_Email")
//    public void consumeEmail(MailMessage mailMessage) {
//        String subject =mailMessage.getSubject();
//        String message =mailMessage.getMessage();
//        String email =mailMessage.getEmail();
//        emailService.sendEmail(email, subject, message);
//    }
//    @KafkaListener(topics = "Notify_Sms")
//    public void consumeSms(SmsMessage smsMessage) {
//        String phoneNumber = smsMessage.getPhoneNumber();
//        String message =smsMessage.getMessage();
//        smsService.sendSms(phoneNumber, message);
//    }
//}
