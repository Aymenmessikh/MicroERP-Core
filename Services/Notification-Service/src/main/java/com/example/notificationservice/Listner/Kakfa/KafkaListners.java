package com.example.notificationservice.Listner.Kakfa;

import com.example.notificationservice.Listner.Modele.UserEvent;
import com.example.notificationservice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaListners {
    private final UserService userService;
    @KafkaListener(topics = "create_user", groupId = "myGroup")
    public void consume(UserEvent userEvent) {
        userService.saveUser(userEvent);
    }
}
