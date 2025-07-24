package com.example.notificationservice.Mapper.NotificationSchedule;

import com.example.notificationservice.Dto.NotificationSchedule.NotificationScheduleRequest;
import com.example.notificationservice.Dto.NotificationSchedule.NotificationScheduleResponse;
import com.example.notificationservice.Entity.NotificationSchedule;

public interface NotificationScheduleMapper {
    NotificationSchedule EntityFromDto(NotificationScheduleRequest notificationScheduleRequest);
    NotificationScheduleResponse DtoFromEntity(NotificationSchedule notificationSchedule);
}
