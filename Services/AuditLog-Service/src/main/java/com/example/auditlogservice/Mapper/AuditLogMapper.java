package com.example.auditlogservice.Mapper;

import com.example.auditlogservice.Dto.AuditEvent;
import com.example.auditlogservice.Dto.AuditResponseDto;
import com.example.auditlogservice.Entity.AuditLog;

public interface AuditLogMapper {
    AuditLog EntityFromDto(AuditEvent auditEvent);
    AuditResponseDto DtoFromEntity(AuditLog auditLog);

}
