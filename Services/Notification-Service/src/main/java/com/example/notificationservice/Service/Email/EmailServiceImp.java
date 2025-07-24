package com.example.notificationservice.Service.Email;

import com.example.notificationservice.Entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailServiceImp implements EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Override
    public void sendEmail(Notification notification) {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(notification.getUser().getEmail());
        message.setSubject(notification.getSubject());
        message.setText(notification.getBody());
        javaMailSender.send(message);
    }
}
