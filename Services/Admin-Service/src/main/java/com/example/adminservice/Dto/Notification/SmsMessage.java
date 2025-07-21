package com.example.adminservice.Dto.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsMessage {
    private String phoneNumber;
    private String message;
}
