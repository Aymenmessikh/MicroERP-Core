package com.example.adminservice.Dto.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailMessage {
    private String email;
    private String subject;
    private String message;
}
