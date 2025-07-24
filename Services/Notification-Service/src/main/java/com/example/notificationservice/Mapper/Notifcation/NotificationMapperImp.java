package com.example.notificationservice.Mapper.Notifcation;

import com.example.notificationservice.Dto.Notification.NotificationResponse;
import com.example.notificationservice.Entity.Notification;
import com.example.notificationservice.Entity.NotificationSchedule;
import com.example.notificationservice.Enums.NotificationStatus;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapperImp implements NotificationMapper{
    @Override
    public NotificationResponse DtoFromEntity(Notification notification) {
        return NotificationResponse.builder()
                .notificationChannel(notification.getNotificationChannel())
                .notificationTime(notification.getNotificationTime())
                .id(notification.getId())
                .body(notification.getBody())
                .subject(notification.getSubject())
                .notificationStatus(notification.getNotificationStatus())
                .build();
    }

    @Override
    public Notification notificationScheduleToNotification(NotificationSchedule schedule) {
        return Notification.builder()
                .notificationChannel(schedule.getNotificationChannel())
                .user(schedule.getUser())
                .notificationTime(schedule.getNotificationTime())
                .subject(schedule.getSubject())
                .body(schedule.getBody())
                .notificationStatus(NotificationStatus.SENT)
                .build();
    }
}
