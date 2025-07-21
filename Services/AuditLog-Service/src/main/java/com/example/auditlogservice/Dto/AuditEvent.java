package com.example.auditlogservice.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuditEvent {
    @JsonProperty("moduleName")
    private String moduleName;
    @JsonProperty("url")
    private String url;
    @JsonProperty("httpMethod")
    private String httpMethod;
    @JsonProperty("objectId")
    private Long objectId;
    @JsonProperty("entityName")
    private String entityName;
    @JsonProperty("methodName")
    private String methodName;
    @JsonProperty("ipAddress")
    private String ipAddress;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("status")
    private String status;
    @JsonProperty("username")
    private String username;
    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("oldEntity")
    private String oldEntity;
    @JsonProperty("newEntity")
    private String newEntity;

    @Override
    public String toString() {
        return "AuditEvent{" +
                "moduleName='" + moduleName + '\'' +
                ", url='" + url + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", entityName='" + entityName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", status='" + status + '\'' +
                ", username='" + username + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", oldEntity=" + oldEntity +
                ", newEntity=" + newEntity +
                '}';
    }
}