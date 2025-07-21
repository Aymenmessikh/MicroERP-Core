package com.example.auditlogservice.Kafka;

import com.example.auditlogservice.Dto.AuditEvent;
import com.example.auditlogservice.Service.AuditLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;

    public KafkaConsumer(ObjectMapper objectMapper, AuditLogService auditLogService) {
        this.objectMapper = objectMapper;
        this.auditLogService = auditLogService;
    }

    @KafkaListener(topics = "audit-topic", groupId = "my-group")
    public void listen(String auditEvent)  {
        try {
            System.out.println("Audit event reçu  : " + auditEvent);
            AuditEvent auditLog = objectMapper.readValue(auditEvent, AuditEvent.class);
            auditLogService.saveAuditLog(auditLog);

            System.out.println("Audit event reçu et converti : " + auditLog.toString());

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}