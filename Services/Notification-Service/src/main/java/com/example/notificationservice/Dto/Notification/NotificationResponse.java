package com.example.notificationservice.Dto.Notification;

import com.example.notificationservice.Dto.User.UserResponse;
import com.example.notificationservice.Entity.User;
import com.example.notificationservice.Enums.NotificationChannel;
import com.example.notificationservice.Enums.NotificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private Long id;
    private String subject;
    private String body;
    private NotificationChannel notificationChannel;
    private NotificationStatus notificationStatus;
    private LocalDateTime notificationTime;
}
