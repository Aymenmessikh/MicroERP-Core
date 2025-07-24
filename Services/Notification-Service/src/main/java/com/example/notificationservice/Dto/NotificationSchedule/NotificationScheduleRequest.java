package com.example.notificationservice.Dto.NotificationSchedule;

import com.example.notificationservice.Enums.NotificationChannel;
import com.example.notificationservice.Enums.NotificationScheduleStatus;
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
public class NotificationScheduleRequest {
    @NotBlank(message = "Subject cannot be null")
    private String subject;
    @NotBlank(message = "Body cannot be null")
    private String body;
    @NotBlank(message = "Notification Chanel code cannot be null")
    private NotificationChannel notificationChannel;
    @NotBlank(message = "Time cannot be null")
    private LocalDateTime notificationTime;
    private String userUuid;
    private Long groupeId;
}
