package com.example.adminservice.Config.AuditLog;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AuditEvent {
    private String moduleName;
    private String url;
    private String httpMethod;
    private String entityName;
    private Long objectId;
    private String methodName;
    private String ipAddress;
    private String timestamp;
    private String status;
    private String username;
    private String errorMessage;
    private String oldEntity;
    private String newEntity;
}
