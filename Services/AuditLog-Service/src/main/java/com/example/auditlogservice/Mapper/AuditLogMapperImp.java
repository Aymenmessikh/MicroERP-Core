package com.example.auditlogservice.Mapper;

import com.example.auditlogservice.Dto.AuditEvent;
import com.example.auditlogservice.Dto.AuditResponseDto;
import com.example.auditlogservice.Entity.AuditLog;
import com.example.auditlogservice.Repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditLogMapperImp implements AuditLogMapper {
    private final AuditRepository auditRepository;
    @Override
    public AuditLog EntityFromDto(AuditEvent auditEvent) {
        String oldEntity;
        if (auditEvent.getHttpMethod().equals("PUT") || auditEvent.getHttpMethod().equals("DELETE")) {
            oldEntity=getOldEntity(auditEvent.getModuleName(),auditEvent.getEntityName(),auditEvent.getObjectId());
        }
        else oldEntity=null;
        return AuditLog.builder()
                .entityName(auditEvent.getEntityName())
                .errorMessage(auditEvent.getErrorMessage())
                .httpMethod(auditEvent.getHttpMethod())
                .url(auditEvent.getUrl())
                .oldEntity(oldEntity)
                .objectId(auditEvent.getObjectId())
                .newEntity(auditEvent.getNewEntity())
                .ipAddress(auditEvent.getIpAddress())
                .moduleName(auditEvent.getModuleName())
                .status(auditEvent.getStatus())
                .timestamp(auditEvent.getTimestamp())
                .username(auditEvent.getUsername())
                .methodName(auditEvent.getMethodName())
                .build();
    }

    @Override
    public AuditResponseDto DtoFromEntity(AuditLog auditLog) {
        return AuditResponseDto.builder()
                .entityName(auditLog.getEntityName())
                .errorMessage(auditLog.getErrorMessage())
                .httpMethod(auditLog.getHttpMethod())
                .url(auditLog.getUrl())
                .objectId(auditLog.getObjectId())
                .moduleName(auditLog.getModuleName())
                .status(auditLog.getStatus())
                .timestamp(auditLog.getTimestamp())
                .username(auditLog.getUsername())
                .methodName(auditLog.getMethodName())
                .id(auditLog.getId())
                .build();
    }

    public String getOldEntity(String moduleName, String entityName, Long objectId) {
        AuditLog auditLog=auditRepository.findLatestAditLog(moduleName, entityName, objectId);
        String id=auditLog.getId().toString();
        return id;
    }
}
