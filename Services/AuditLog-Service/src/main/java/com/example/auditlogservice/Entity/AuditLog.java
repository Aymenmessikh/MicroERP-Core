package com.example.auditlogservice.Entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(columnDefinition = "TEXT")
    private String oldEntity;
    @Column(columnDefinition = "TEXT")
    private String newEntity;
}
