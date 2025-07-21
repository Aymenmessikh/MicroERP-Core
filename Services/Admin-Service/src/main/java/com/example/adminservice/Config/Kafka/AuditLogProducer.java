package com.example.adminservice.Config.Kafka;

import com.example.adminservice.Config.AuditLog.AuditEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogProducer {
    private static final Logger logger = LoggerFactory.getLogger(AuditLogProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    public void sendAuditEvent(AuditEvent auditEvent) throws JsonProcessingException {
        // Convertir l'objet en JSON String
        String jsonString = objectMapper.writeValueAsString(auditEvent);
        kafkaTemplate.send("audit-topic", jsonString);

    }
}