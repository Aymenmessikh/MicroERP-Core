package com.example.notificationservice.Dto.NotificationTemplate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationTemplateRequest {

    @NotBlank(message = "Template code cannot be null")
    private String templateCode;
    @NotBlank(message = "Subject cannot be null")
    private String subject;
    @NotBlank(message = "Content cannot be null")
    private String content;
}
