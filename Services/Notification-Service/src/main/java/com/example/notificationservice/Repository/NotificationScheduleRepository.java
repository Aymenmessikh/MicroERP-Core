package com.example.notificationservice.Repository;

import com.example.notificationservice.Entity.NotificationSchedule;
import com.example.notificationservice.Enums.NotificationScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationScheduleRepository extends JpaRepository<NotificationSchedule,Long>,
        JpaSpecificationExecutor<NotificationSchedule> {
    List<NotificationSchedule>
    findByNotificationTimeGreaterThanEqualAndNotificationTimeLessThanEqualAndStatus(LocalDateTime start, LocalDateTime end, NotificationScheduleStatus status);
}
