package com.example.notificationservice.Mapper.NotificationTemplate;

import com.example.notificationservice.Dto.NotificationTemplate.NotificationTemplateRequest;
import com.example.notificationservice.Dto.NotificationTemplate.NotificationTemplateResponse;
import com.example.notificationservice.Entity.NotificationTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationTemplateMapperImp implements NotificationTemplateMapper{
    @Override
    public NotificationTemplate EntityFromDto(NotificationTemplateRequest notificationTemplateRequest) {
        return NotificationTemplate.builder()
                .active(true)
                .content(notificationTemplateRequest.getContent())
                .templateCode(notificationTemplateRequest.getTemplateCode())
                .subject(notificationTemplateRequest.getSubject())
                .build();
    }

    @Override
    public NotificationTemplateResponse DtoFromEntity(NotificationTemplate notificationTemplate) {
        return NotificationTemplateResponse.builder()
                .active(notificationTemplate.getActive())
                .content(notificationTemplate.getContent())
                .id(notificationTemplate.getId())
                .subject(notificationTemplate.getSubject())
                .templateCode(notificationTemplate.getTemplateCode())
                .build();
    }
}
