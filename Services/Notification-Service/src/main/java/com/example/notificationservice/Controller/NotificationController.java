package com.example.notificationservice.Controller;

import com.example.notificationservice.Dto.Notification.NotificationResponse;
import com.example.notificationservice.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/notification")
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifictionForUser(){
        List<NotificationResponse> notificationResponses=notificationService.getAllNotificationForUser();
        return new ResponseEntity<>(notificationResponses, HttpStatus.OK);
    }
    @GetMapping("seen")
    public ResponseEntity<List<NotificationResponse>> getAllNotSeenNotifictionForUser(){
        List<NotificationResponse> notificationResponses=notificationService.getAllNotSennNotificationForUser();
        return new ResponseEntity<>(notificationResponses, HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<NotificationResponse> getNotifictionById(@PathVariable Long id){
        NotificationResponse notificationResponse=notificationService.getNotificationById(id);
        return new ResponseEntity<>(notificationResponse, HttpStatus.OK);
    }
    @GetMapping("numbers")
    public ResponseEntity<Integer> getNotSeenNotifictionNumberForUser(){
        Integer numbers=notificationService.getNotSeenNotificationNumber();
        return new ResponseEntity<>(numbers, HttpStatus.OK);
    }

}
