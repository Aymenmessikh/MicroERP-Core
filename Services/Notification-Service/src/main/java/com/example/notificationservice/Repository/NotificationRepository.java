package com.example.notificationservice.Repository;

import com.example.notificationservice.Entity.Notification;
import com.example.notificationservice.Entity.User;
import com.example.notificationservice.Enums.NotificationChannel;
import com.example.notificationservice.Enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long>,
        JpaSpecificationExecutor <Notification>{
    List<Notification> findAllByUser(User user);
    Integer countByNotificationStatusAndUserAndNotificationChannelIn(NotificationStatus sent, User user, List<NotificationChannel> channels);

}
