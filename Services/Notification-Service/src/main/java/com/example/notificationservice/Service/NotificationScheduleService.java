package com.example.notificationservice.Service;

import com.example.notificationservice.Dto.NotificationSchedule.NotificationScheduleRequest;
import com.example.notificationservice.Dto.NotificationSchedule.NotificationScheduleResponse;
import com.example.notificationservice.Entity.NotificationSchedule;
import com.example.notificationservice.Exeptions.InvalidNotificationTimeException;
import com.example.notificationservice.Exeptions.MyResourceNotFoundException;
import com.example.notificationservice.Mapper.NotificationSchedule.NotificationScheduleMapper;
import com.example.notificationservice.Repository.NotificationScheduleRepository;
import com.example.notificationservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationScheduleService {
    private final NotificationScheduleRepository notificationScheduleRepository;
    private final NotificationScheduleMapper notificationScheduleMapper;
    private final UserRepository userRepository;
    private final GroupeService groupeService;
    public NotificationScheduleResponse createNotificationSchedule(NotificationScheduleRequest notificationScheduleRequest) {
        if (notificationScheduleRequest.getNotificationTime().isAfter(LocalDateTime.now())) {
            if (notificationScheduleRequest.getUserUuid() != "") {
                NotificationSchedule notificationSchedule = notificationScheduleRepository
                        .save(notificationScheduleMapper.EntityFromDto(notificationScheduleRequest));
                return notificationScheduleMapper.DtoFromEntity(notificationSchedule);
            } else if (notificationScheduleRequest.getGroupeId() != -1) {
                Set<String> uuids = groupeService.getUserUuidByGroupe(notificationScheduleRequest.getGroupeId());

                List<NotificationSchedule> notificationSchedules = uuids.stream()
                        .map(uuid -> {
                            notificationScheduleRequest.setUserUuid(uuid);
                            return notificationScheduleMapper.EntityFromDto(notificationScheduleRequest);
                        })
                        .collect(Collectors.toList());

                // Optionally, save all created NotificationSchedules and return a response
                notificationScheduleRepository.saveAll(notificationSchedules);
                return null; // Return appropriate response as needed (e.g., a list of responses)
            } else {
                throw new InvalidNotificationTimeException("UserId and GroupeId are both null");
            }
        } else {
            throw new InvalidNotificationTimeException("Notification time must be after the current time.");
        }
    }

    public List<NotificationScheduleResponse> getAllNotificationSchedule(){
        List<NotificationScheduleResponse> notificationScheduleResponses=notificationScheduleRepository
                .findAll().stream().map(notificationScheduleMapper::DtoFromEntity)
                .collect(Collectors.toList());
        return notificationScheduleResponses;
    }
    public NotificationScheduleResponse getNotificationScheduleById(Long id){
        NotificationScheduleResponse notificationScheduleResponse=notificationScheduleMapper
                .DtoFromEntity(notificationScheduleRepository
                        .findById(id).orElseThrow(()->new MyResourceNotFoundException(
                                "Notification Schedule not found with id : "+id
                        )));
        return notificationScheduleResponse;
    }
    public NotificationScheduleResponse updateNotificationSchedule(
            Long id, NotificationScheduleRequest notificationScheduleRequest){
        NotificationSchedule notificationSchedule=notificationScheduleRepository.findById(id)
                .orElseThrow(()->new MyResourceNotFoundException
                        ("Notification Schedule not found with id : "+id));
        notificationSchedule.setNotificationChannel(notificationScheduleRequest.getNotificationChannel());
        notificationSchedule.setNotificationTime(notificationScheduleRequest.getNotificationTime());
        notificationSchedule.setSubject(notificationScheduleRequest.getSubject());
        notificationSchedule.setBody(notificationScheduleRequest.getBody());
        notificationSchedule.setUser(userRepository.findByUuid(notificationScheduleRequest.getUserUuid())
                .orElseThrow(()->new MyResourceNotFoundException("User Not Found Exeption with id :"
                        +notificationScheduleRequest.getUserUuid())));
        return notificationScheduleMapper.DtoFromEntity(notificationSchedule);
    }
    public void deleteNotificationSchedule(Long id){
        NotificationSchedule notificationSchedule=notificationScheduleRepository.findById(id)
                .orElseThrow(()->new MyResourceNotFoundException
                        ("Notification Schedule not found with id : "+id));
        notificationScheduleRepository.delete(notificationSchedule);
    }
}
