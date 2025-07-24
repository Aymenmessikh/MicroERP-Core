package com.example.notificationservice.Controller;

import com.example.notificationservice.Dto.NotificationTemplate.NotificationTemplateRequest;
import com.example.notificationservice.Dto.NotificationTemplate.NotificationTemplateResponse;
import com.example.notificationservice.Service.NotificationTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/notification/notificationTemplate")
@RequiredArgsConstructor
public class NotificationTemplateController {
    private final NotificationTemplateService notificationTemplateService;
    @PostMapping
    @PreAuthorize("@authz.hasCustomAuthority('create_notification_Template')")
    public ResponseEntity<NotificationTemplateResponse> createNotificationTemplate(@Valid
            @RequestBody NotificationTemplateRequest notificationTemplateRequest
            ){
        NotificationTemplateResponse notificationTemplateResponse=notificationTemplateService
                .createNotificationTemplate(notificationTemplateRequest);
        return new ResponseEntity<>(notificationTemplateResponse, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<NotificationTemplateResponse>> getAllNotificationTemplate(){
        List<NotificationTemplateResponse> notificationTemplateResponses=
                notificationTemplateService.getAllNotifications();
        return new ResponseEntity<>(notificationTemplateResponses,HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<NotificationTemplateResponse> getNotificationTemplateById(@PathVariable Long id){
        NotificationTemplateResponse notificationTemplateResponses=
                notificationTemplateService.getNotificationTemplateById(id);
        return new ResponseEntity<>(notificationTemplateResponses,HttpStatus.OK);
    }
    @GetMapping("byCode/{code}")
    public ResponseEntity<NotificationTemplateResponse> getNotificationTemplateByCode(@PathVariable String code){
        NotificationTemplateResponse notificationTemplateResponses=
                notificationTemplateService.getNotificationTemplateByCode(code);
        return new ResponseEntity<>(notificationTemplateResponses,HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity<NotificationTemplateResponse> updateNotificationTemplate(
            @Valid
            @RequestBody NotificationTemplateRequest notificationTemplateRequest,
            @PathVariable Long id
    ){
        NotificationTemplateResponse notificationTemplateResponse=notificationTemplateService
                .updateNotificationTemplate(notificationTemplateRequest,id);
        return new ResponseEntity<>(notificationTemplateResponse, HttpStatus.CREATED);
    }
    @PutMapping("active/{id}")
    public ResponseEntity<NotificationTemplateResponse> enableNotificationTemplate(@PathVariable Long id){
        NotificationTemplateResponse notificationTemplateResponse=notificationTemplateService
                .enableNotificationTemplate(id);
        return new ResponseEntity<>(notificationTemplateResponse,HttpStatus.OK);
    }
    @PutMapping("disactive/{id}")
    public ResponseEntity<NotificationTemplateResponse> disableNotificationTemplate(@PathVariable Long id){
        NotificationTemplateResponse notificationTemplateResponse=notificationTemplateService
                .disableNotificationTemplate(id);
        return new ResponseEntity<>(notificationTemplateResponse,HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public void deleteNotificationTemplateById(@PathVariable Long id){
        notificationTemplateService.deleteNotificationTemplate(id);
    }
}
