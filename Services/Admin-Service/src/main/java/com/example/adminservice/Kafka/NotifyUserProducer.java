//package com.example.adminservice.Kafka;
//
//import com.example.adminservice.Dto.Notification.MailMessage;
//import com.example.adminservice.Dto.Notification.SmsMessage;
//import com.example.adminservice.Dto.User.UserRequest;
//import com.example.adminservice.Entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//@Component
//@RequiredArgsConstructor
//public class NotifyUserProducer {
//    private static final String TOPICEMAIL = "Notify_Email";
//    private static final String TOPICSMS = "Notify_Sms";
//    private static final String CREATE_USER_TOPIC = "create_user";
//
//    private final KafkaTemplate<String, MailMessage> mailTemplate;
//    private final KafkaTemplate<String, SmsMessage> smsTemplate;
//    private final KafkaTemplate<String, User> createUser;
//
//    public void sendEmailMessage(MailMessage mailMessage) {
//        mailTemplate.send(TOPICEMAIL, mailMessage);
//    }
//
//    public void sendSmsMessage(SmsMessage smsMessage) {
//        smsTemplate.send(TOPICSMS, smsMessage);
//    }
//    public void createUser(User user){
//        createUser.send(CREATE_USER_TOPIC,user);
//    }
//}
