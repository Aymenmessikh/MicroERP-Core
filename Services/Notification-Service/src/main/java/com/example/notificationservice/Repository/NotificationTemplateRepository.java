package com.example.notificationservice.Repository;

import com.example.notificationservice.Entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate,Long> {

    Optional<NotificationTemplate> findByTemplateCode(String code);
}
