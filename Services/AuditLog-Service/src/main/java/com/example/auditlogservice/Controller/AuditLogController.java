package com.example.auditlogservice.Controller;

import com.example.auditlogservice.Config.filter.clause.Clause;
import com.example.auditlogservice.Config.filter.clause.ClauseOneArg;
import com.example.auditlogservice.Config.filter.handlerMethodeArgumentResolver.Critiria;
import com.example.auditlogservice.Config.filter.handlerMethodeArgumentResolver.SearchValue;
import com.example.auditlogservice.Config.filter.handlerMethodeArgumentResolver.SortParam;
import com.example.auditlogservice.Dto.AuditResponseDto;
import com.example.auditlogservice.Service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/audit-log")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogService auditLogService;

    @GetMapping
//    @PreAuthorize("@authz.hasCustomAuthority('READ_AUDIT_LOG')")
    public ResponseEntity<PageImpl<AuditResponseDto>> getAuditLog(@Critiria List<Clause> filter,
                                                                  @SearchValue ClauseOneArg searchValue,
                                                                  @SortParam PageRequest pageRequest) {
        filter.add(searchValue);
        PageImpl<AuditResponseDto> auditResponseDtos = auditLogService.getAllAuditLogs(filter, pageRequest);
        return new ResponseEntity<>(auditResponseDtos, HttpStatus.OK);
    }
}
