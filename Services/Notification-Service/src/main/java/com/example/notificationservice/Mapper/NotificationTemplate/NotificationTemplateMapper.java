package com.example.notificationservice.Mapper.NotificationTemplate;

import com.example.notificationservice.Dto.NotificationTemplate.NotificationTemplateRequest;
import com.example.notificationservice.Dto.NotificationTemplate.NotificationTemplateResponse;
import com.example.notificationservice.Entity.NotificationTemplate;

public interface NotificationTemplateMapper {
    NotificationTemplate EntityFromDto(NotificationTemplateRequest notificationTemplateRequest);
    NotificationTemplateResponse DtoFromEntity(NotificationTemplate notificationTemplate);
}
