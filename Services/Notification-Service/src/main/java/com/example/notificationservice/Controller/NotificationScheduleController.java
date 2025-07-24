package com.example.notificationservice.Controller;

import com.example.notificationservice.Dto.NotificationSchedule.NotificationScheduleRequest;
import com.example.notificationservice.Dto.NotificationSchedule.NotificationScheduleResponse;
import com.example.notificationservice.Service.NotificationScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/notification/notificationSchedule")
public class NotificationScheduleController {
    private final NotificationScheduleService notificationScheduleService;
    @PostMapping
    public ResponseEntity<NotificationScheduleResponse> createNotificationSchedule(
            @RequestBody NotificationScheduleRequest notificationScheduleRequest
            ){
        NotificationScheduleResponse notificationScheduleResponse=notificationScheduleService
                .createNotificationSchedule(notificationScheduleRequest);
        return new ResponseEntity<>(notificationScheduleResponse, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<NotificationScheduleResponse>> getAllNotificationSchedule(){
        List<NotificationScheduleResponse> notificationScheduleResponses=notificationScheduleService
                .getAllNotificationSchedule();
        return new ResponseEntity<>(notificationScheduleResponses,HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<NotificationScheduleResponse> getNotificationScheduleById(
            @PathVariable Long id){
        NotificationScheduleResponse notificationScheduleResponse=notificationScheduleService
                .getNotificationScheduleById(id);
        return new ResponseEntity<>(notificationScheduleResponse,HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity<NotificationScheduleResponse> updateNotificationSchedule(
            @PathVariable Long id,@RequestBody NotificationScheduleRequest notificationScheduleRequest){
        NotificationScheduleResponse notificationScheduleResponse=notificationScheduleService
                .updateNotificationSchedule(id,notificationScheduleRequest);
        return new ResponseEntity<>(notificationScheduleResponse,HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteNotificationSchedule(
            @PathVariable Long id){
        notificationScheduleService.deleteNotificationSchedule(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
