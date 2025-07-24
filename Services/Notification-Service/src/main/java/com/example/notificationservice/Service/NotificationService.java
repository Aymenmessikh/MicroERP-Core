package com.example.notificationservice.Service;

import com.example.notificationservice.Dto.Notification.NotificationResponse;
import com.example.notificationservice.Entity.Notification;
import com.example.notificationservice.Entity.NotificationSchedule;
import com.example.notificationservice.Entity.User;
import com.example.notificationservice.Enums.NotificationChannel;
import com.example.notificationservice.Enums.NotificationScheduleStatus;
import com.example.notificationservice.Enums.NotificationStatus;
import com.example.notificationservice.Exeptions.MyResourceNotFoundException;
import com.example.notificationservice.Listner.Modele.NotificationPayload;
import com.example.notificationservice.Listner.Modele.PlaceHolder;
import com.example.notificationservice.Mapper.Notifcation.NotificationMapper;
import com.example.notificationservice.Repository.NotificationRepository;
import com.example.notificationservice.Repository.NotificationScheduleRepository;
import com.example.notificationservice.Repository.NotificationTemplateRepository;
import com.example.notificationservice.Repository.UserRepository;
import com.example.notificationservice.Service.Email.EmailService;
import com.example.notificationservice.Service.InApp.InAppNotifcationService;
import com.example.notificationservice.Service.Sms.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final UserRepository userRepository;
    private final NotificationScheduleRepository notificationScheduleRepository;
    private final GroupeService groupeService;
    private final EmailService emailService;
    private final SmsService smsService;
    private final InAppNotifcationService inAppNotifcationService;
    private final NotificationMapper notificationMapper;

    public void notification(NotificationPayload notificationPayload) {
        String subject;
        String body;
        if (notificationPayload.getSubjectPlaceHolders() != null) {
            subject = replacePlaceholders(notificationTemplateRepository.findByTemplateCode(notificationPayload
                            .getTemplateCode()).orElseThrow(() -> new MyResourceNotFoundException("Template Notification " +
                            "not found with code : " + notificationPayload.getTemplateCode()))
                    .getSubject(), notificationPayload.getSubjectPlaceHolders());
        } else {
            subject = notificationTemplateRepository
                    .findByTemplateCode(notificationPayload.getTemplateCode())
                    .orElseThrow(() -> new MyResourceNotFoundException("Template Notification not found " +
                            "with code : " + notificationPayload.getTemplateCode())).getSubject();
        }
        if (notificationPayload.getBodyPlaceHolders() != null) {
            body = replacePlaceholders(notificationTemplateRepository
                    .findByTemplateCode(notificationPayload.getTemplateCode())
                    .orElseThrow(() -> new MyResourceNotFoundException
                            ("Template Notification not found :" + notificationPayload.getTemplateCode()))
                    .getContent(), notificationPayload.getBodyPlaceHolders());
        } else {
            body = notificationTemplateRepository
                    .findByTemplateCode(notificationPayload.getTemplateCode())
                    .orElseThrow(() ->
                            new MyResourceNotFoundException("Template Notification not found :" + notificationPayload.getTemplateCode()))
                    .getContent();
        }
        if (notificationPayload.getUserUuid()!=null) {
            User user = userRepository.findByUuid(notificationPayload.getUserUuid())
                    .orElseThrow(() -> new MyResourceNotFoundException
                            ("user not found with Uuid : " + notificationPayload.getUserUuid()));
            Notification notification = Notification.builder()
                    .notificationChannel(notificationPayload.getChannel())
                    .notificationTime(notificationPayload.getTime())
                    .body(body)
                    .subject(subject)
                    .notificationStatus(NotificationStatus.SENT)
                    .user(user)
                    .build();
            notificationRepository.save(notification);
            pushNotification(notification);
        } else if (notificationPayload.getGroupeId()!=null){
            Set<String> uuids=groupeService.getUserUuidByGroupe(notificationPayload.getGroupeId());
            List<Notification> notifications = uuids.stream()
                    .map(uuid -> {
                        User user = userRepository.findByUuid(uuid)
                                .orElseThrow(() -> new MyResourceNotFoundException
                                        ("user not found with Uuid : " + notificationPayload.getUserUuid()));
                        return Notification.builder()
                                .notificationChannel(notificationPayload.getChannel())
                                .notificationTime(notificationPayload.getTime())
                                .body(body)
                                .subject(subject)
                                .notificationStatus(NotificationStatus.SENT)
                                .user(user)
                                .build();
                    }).collect(Collectors.toList());

            notifications.forEach(notificationRepository::save);
            notifications.forEach(this::pushNotification);
        }
    }

    public void pushNotification(Notification notification) {
        NotificationChannel notificationChannel = notification.getNotificationChannel();
        switch (notificationChannel) {
            case EMAIL -> {
                emailService.sendEmail(notification);
            }
            case SMS -> {
                smsService.sendSms(notification);
            }
            case IN_APP -> {
                inAppNotifcationService.inAppPush("/topic/notifications",notification);
            }
            case ALL -> {
                emailService.sendEmail(notification);
                smsService.sendSms(notification);
                inAppNotifcationService.inAppPush("/topic/notifications",notification);
            }
        }
    }

    public static String replacePlaceholders(String inputString, PlaceHolder[] replacements) {
        Pattern pattern = Pattern.compile("\\{\\{(\\d+)\\}\\}"); // Pattern to match placeholders
        Matcher matcher = pattern.matcher(inputString);
        StringBuffer sb = new StringBuffer();

        // Iterate through the placeholders and replace them
        while (matcher.find()) {
            int key = Integer.parseInt(matcher.group(1));
            String value = findValueForKey(replacements, key);
            if (value == null) {
                // Handle missing placeholder value
                throw new IllegalArgumentException("Replacement value not found for placeholder: {{" + key + "}}");
            }
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private static String findValueForKey(PlaceHolder[] replacements, int key) {
        for (PlaceHolder placeholder : replacements) {
            if (placeholder.getKey() == key) {
                return placeholder.getValue();
            }
        }
        return null;
    }
    public List<NotificationResponse> getAllNotificationForUser() {
        String username = getUserNameForCurrentUser();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new MyResourceNotFoundException
                        ("User not found with userName : " + username));
        List<NotificationResponse> notificationResponses = notificationRepository
                .findAllByUser(user).stream().map(notificationMapper::DtoFromEntity)
                .collect(Collectors.toList());
        return notificationResponses;
    }
    public List<NotificationResponse> getAllNotSennNotificationForUser() {
        String username = getUserNameForCurrentUser();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new MyResourceNotFoundException
                        ("User not found with userName : " + username));
        List<NotificationResponse> notificationResponses = notificationRepository
                .findAllByUser(user).stream().filter(notification -> {
                    return notification.getNotificationStatus()!=NotificationStatus.SEEN;
                }).map(notificationMapper::DtoFromEntity)
                .collect(Collectors.toList());
        return notificationResponses;
    }
    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException
                        ("Notification not found with id: "+id));
        ChnageNotificationStatus(notification);
        return notificationMapper.DtoFromEntity(notification);
    }
    private void ChnageNotificationStatus(Notification notification){
        notification.setNotificationStatus(NotificationStatus.SEEN);
        notificationRepository.save(notification);
    }
    public Integer getNotSeenNotificationNumber() {
        String username = getUserNameForCurrentUser();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new MyResourceNotFoundException
                        ("User not found with userName : " + username));
        List<NotificationChannel> channels = new ArrayList<>();
        channels.add(NotificationChannel.IN_APP);
        channels.add(NotificationChannel.ALL);
        return notificationRepository
                .countByNotificationStatusAndUserAndNotificationChannelIn(NotificationStatus.SENT, user, channels);
    }
    private String getUserNameForCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) authentication;
        Jwt jwt = jwtAuthentication.getToken();

        String username = (String) jwt.getClaims().get("preferred_username");
        return username;
    }
    @Scheduled(fixedRate = 300000) /* Check every 5 minutes for new notifications */
    public void checkForScheduledNotifications() {

        List<NotificationSchedule> pendingNotifications = fetchPendingNotifications();

        for (NotificationSchedule notificationSchedule : pendingNotifications) {
            /* save the notification in db */
            Notification entity = notificationRepository
                    .save(notificationMapper.notificationScheduleToNotification(notificationSchedule));

            /* edit status of scheduled notification to SENT */
            notificationScheduleRepository.findById(notificationSchedule.getId())
                    .orElseThrow(()->new MyResourceNotFoundException
                            ("Notification Schedule not found with Id: "+notificationSchedule.getId()))
                    .setStatus(NotificationScheduleStatus.SENT);

            pushNotification(entity);

        }
    }
    public List<NotificationSchedule> fetchPendingNotifications() {
        return notificationScheduleRepository.
                findByNotificationTimeGreaterThanEqualAndNotificationTimeLessThanEqualAndStatus
                        (LocalDateTime.now(), LocalDateTime.now().plusMinutes(5),
                                NotificationScheduleStatus.EN_ATTENTE);
    }
}
