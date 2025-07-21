package com.example.auditlogservice.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditResponseDto {
    private Long id;
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
//    @Column(columnDefinition = "TEXT")
//    private String oldEntity;
//    @Column(columnDefinition = "TEXT")
//    private String newEntity;
}
