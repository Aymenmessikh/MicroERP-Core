package com.example.notificationservice.Controller;

import com.example.notificationservice.Config.Security.AuthorizationLogic;
import com.example.notificationservice.Entity.Notification;
import com.example.notificationservice.Listner.Modele.NotificationPayload;
import com.example.notificationservice.Service.GroupeService;
import com.example.notificationservice.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/notification/test")
@RequiredArgsConstructor
public class TestController {
    private final AuthorizationLogic authorizationLogic;
    private final NotificationService notificationService;
    private final GroupeService groupeService;
    @GetMapping
    public ResponseEntity<Object> getPermissionForUser(@RequestParam String authority) {
        try {
            return new ResponseEntity<>(authorizationLogic.hasCustomAuthority(authority), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @PostMapping
//    public ResponseEntity<List<Notification>> notification(@RequestBody NotificationPayload notificationPayload){
//        List<Notification> notification=notificationService.notification(notificationPayload);
//        return new ResponseEntity<>(notification,HttpStatus.CREATED);
//    }
    @GetMapping("e/{id}")
    public ResponseEntity<Set<String>> get(@PathVariable Long id){
        Set<String> list=groupeService.getUserUuidByGroupe(id);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
}
