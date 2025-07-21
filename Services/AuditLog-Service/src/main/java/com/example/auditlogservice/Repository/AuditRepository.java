package com.example.auditlogservice.Repository;

import com.example.auditlogservice.Entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {
    @Query(value = "SELECT * FROM audit_log " +
            "WHERE module_name = :moduleName " +
            "AND entity_name = :entityName " +
            "AND object_id = :objectId " +
            "ORDER BY timestamp DESC LIMIT 1", nativeQuery = true)
    AuditLog findLatestAditLog(String moduleName, String entityName, Long objectId);
}
