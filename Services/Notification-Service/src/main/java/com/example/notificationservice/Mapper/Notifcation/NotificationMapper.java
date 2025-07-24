package com.example.notificationservice.Mapper.Notifcation;

import com.example.notificationservice.Dto.Notification.NotificationResponse;
import com.example.notificationservice.Entity.Notification;
import com.example.notificationservice.Entity.NotificationSchedule;

public interface NotificationMapper {
    NotificationResponse DtoFromEntity(Notification notification);
    Notification notificationScheduleToNotification(NotificationSchedule schedule);
}
