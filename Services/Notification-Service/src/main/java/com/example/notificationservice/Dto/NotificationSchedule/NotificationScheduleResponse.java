package com.example.notificationservice.Dto.NotificationSchedule;

import com.example.notificationservice.Dto.User.UserResponse;
import com.example.notificationservice.Entity.User;
import com.example.notificationservice.Enums.NotificationChannel;
import com.example.notificationservice.Enums.NotificationScheduleStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationScheduleResponse {
    private Long id;
    private String subject;
    private String body;
    private LocalDateTime notificationTime;
    private NotificationChannel notificationChannel;
    private NotificationScheduleStatus status;
    private UserResponse userResponse;
}
