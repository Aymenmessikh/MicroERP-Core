package com.example.auditlogservice.Service;

import com.example.auditlogservice.Config.filter.clause.Clause;
import com.example.auditlogservice.Config.filter.specification.GenericSpecification;
import com.example.auditlogservice.Dto.AuditEvent;
import com.example.auditlogservice.Dto.AuditResponseDto;
import com.example.auditlogservice.Entity.AuditLog;
import com.example.auditlogservice.Mapper.AuditLogMapper;
import com.example.auditlogservice.Repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditRepository auditRepository;
    private final AuditLogMapper auditLogMapper;

    public void saveAuditLog(AuditEvent auditEvent) {
        AuditLog auditLog = auditLogMapper.EntityFromDto(auditEvent);
        auditRepository.save(auditLog);
    }

    public PageImpl<AuditResponseDto> getAllAuditLogs(List<Clause> filter, PageRequest pageRequest) {
        Specification<AuditLog> specification = new GenericSpecification<>(filter);
        Page<AuditLog> page = auditRepository.findAll(specification, pageRequest);
        List<AuditResponseDto> auditLogs = page.getContent().stream().map(auditLogMapper::DtoFromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(auditLogs, pageRequest, page.getTotalElements());
    }
}
