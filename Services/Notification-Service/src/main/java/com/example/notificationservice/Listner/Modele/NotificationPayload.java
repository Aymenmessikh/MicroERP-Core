package com.example.notificationservice.Listner.Modele;

import com.example.notificationservice.Enums.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPayload {
    private String userUuid;
    private Long groupeId;
    private String templateCode;
    private PlaceHolder[] subjectPlaceHolders;
    private PlaceHolder[] bodyPlaceHolders;
    private NotificationChannel channel;
    private LocalDateTime time;
}
