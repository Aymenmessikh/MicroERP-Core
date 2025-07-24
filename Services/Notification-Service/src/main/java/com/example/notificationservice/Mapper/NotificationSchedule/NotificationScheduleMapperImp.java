package com.example.notificationservice.Mapper.NotificationSchedule;

import com.example.notificationservice.Dto.NotificationSchedule.NotificationScheduleRequest;
import com.example.notificationservice.Dto.NotificationSchedule.NotificationScheduleResponse;
import com.example.notificationservice.Entity.NotificationSchedule;
import com.example.notificationservice.Entity.User;
import com.example.notificationservice.Enums.NotificationScheduleStatus;
import com.example.notificationservice.Exeptions.MyResourceNotFoundException;
import com.example.notificationservice.Mapper.User.UserMapper;
import com.example.notificationservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduleMapperImp implements NotificationScheduleMapper{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public NotificationSchedule EntityFromDto(NotificationScheduleRequest notificationScheduleRequest) {
        User user=userRepository.findByUuid(notificationScheduleRequest.getUserUuid()).orElseThrow(
                ()->
                        new MyResourceNotFoundException
                                ("user not found by UUID :"+notificationScheduleRequest.getUserUuid()));
        return NotificationSchedule.builder()
                .body(notificationScheduleRequest.getBody())
                .subject(notificationScheduleRequest.getSubject())
                .notificationTime(notificationScheduleRequest.getNotificationTime())
                .notificationChannel(notificationScheduleRequest.getNotificationChannel())
                .status(NotificationScheduleStatus.EN_ATTENTE)
                .user(user)
                .build();
    }

    @Override
    public NotificationScheduleResponse DtoFromEntity(NotificationSchedule notificationSchedule) {
        return NotificationScheduleResponse.builder()
                .body(notificationSchedule.getBody())
                .id(notificationSchedule.getId())
                .notificationChannel(notificationSchedule.getNotificationChannel())
                .notificationTime(notificationSchedule.getNotificationTime())
                .status(notificationSchedule.getStatus())
                .subject(notificationSchedule.getSubject())
                .userResponse(userMapper.DtoFromEntity(notificationSchedule.getUser()))
                .build();
    }
}
