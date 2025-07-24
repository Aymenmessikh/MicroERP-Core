package com.example.notificationservice.Dto.NotificationTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationTemplateResponse {
    private Long id;
    private String templateCode;
    private String subject;
    private String content;
    private Boolean active;
}
