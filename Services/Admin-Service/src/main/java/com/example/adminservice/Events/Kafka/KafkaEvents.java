//package com.example.adminservice.Events.Kafka;
//
//import com.example.adminservice.Events.Modele.UserEvent;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class KafkaEvents {
//    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
//
//    public void sendUserToNotificationService(UserEvent userEvent) {
//        kafkaTemplate.send("create_user",userEvent);
//    }
//}
