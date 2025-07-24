package com.example.notificationservice.Service;

import com.example.notificationservice.Dto.NotificationTemplate.NotificationTemplateRequest;
import com.example.notificationservice.Dto.NotificationTemplate.NotificationTemplateResponse;
import com.example.notificationservice.Entity.NotificationTemplate;
import com.example.notificationservice.Exeptions.MyResourceNotFoundException;
import com.example.notificationservice.Exeptions.RessourceAlreadyEnabledException;
import com.example.notificationservice.Mapper.NotificationTemplate.NotificationTemplateMapper;
import com.example.notificationservice.Repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationTemplateService {
    private final NotificationTemplateRepository notificationTemplateRepository;
    public final NotificationTemplateMapper notificationTemplateMapper;
    public NotificationTemplateResponse createNotificationTemplate(NotificationTemplateRequest notificationTemplateRequest){
        NotificationTemplate notificationTemplate=notificationTemplateRepository.
                save(notificationTemplateMapper.EntityFromDto(notificationTemplateRequest));
        return notificationTemplateMapper.DtoFromEntity(notificationTemplate);
    }
    public List<NotificationTemplateResponse> getAllNotifications(){
        List<NotificationTemplateResponse> notificationTemplateResponses=
                notificationTemplateRepository.findAll().stream().map(notificationTemplateMapper::DtoFromEntity)
                        .collect(Collectors.toList());
        return notificationTemplateResponses;
    }
    public NotificationTemplateResponse getNotificationTemplateById(Long id){
        NotificationTemplate notificationTemplate=notificationTemplateRepository.findById(id)
                .orElseThrow(()->new MyResourceNotFoundException("Notification Template not found with code: "+id));
        return notificationTemplateMapper.DtoFromEntity(notificationTemplate);
    }
    public NotificationTemplateResponse getNotificationTemplateByCode(String code){
        NotificationTemplate notificationTemplate=notificationTemplateRepository.findByTemplateCode(code)
                .orElseThrow(()->new MyResourceNotFoundException("Notification Template not found with code: "+code));
        return notificationTemplateMapper.DtoFromEntity(notificationTemplate);
    }
    public NotificationTemplateResponse updateNotificationTemplate(NotificationTemplateRequest
                                                                           notificationTemplateRequest,Long id){
        NotificationTemplate notificationTemplate=notificationTemplateRepository.findById(id)
                .orElseThrow(()->new MyResourceNotFoundException("Notification Template not found with code: "+id));
        notificationTemplate.setTemplateCode(notificationTemplateRequest.getTemplateCode());
        notificationTemplate.setContent(notificationTemplateRequest.getContent());
        notificationTemplate.setSubject(notificationTemplateRequest.getSubject());
        notificationTemplateRepository.save(notificationTemplate);
        return notificationTemplateMapper.DtoFromEntity(notificationTemplate);
    }
    public NotificationTemplateResponse enableNotificationTemplate(Long id){
        NotificationTemplate notificationTemplate=notificationTemplateRepository.findById(id)
                .orElseThrow(()->new MyResourceNotFoundException("Notification Template not found with code: "+id));
        if (notificationTemplate.getActive()==false){
            notificationTemplate.setActive(true);
            return notificationTemplateMapper.DtoFromEntity(notificationTemplate);
        } throw new RessourceAlreadyEnabledException("Notification Template with ID " + id + " is already disabled");
    }
    public NotificationTemplateResponse disableNotificationTemplate(Long id){
        NotificationTemplate notificationTemplate=notificationTemplateRepository.findById(id)
                .orElseThrow(()->new MyResourceNotFoundException("Notification Template not found with code: "+id));
        if (notificationTemplate.getActive()==true){
            notificationTemplate.setActive(false);
            return notificationTemplateMapper.DtoFromEntity(notificationTemplate);
        } throw new RessourceAlreadyEnabledException("Notification Template with ID " + id + " is already disabled");
    }
    public void  deleteNotificationTemplate(Long id){
        NotificationTemplate notificationTemplate=notificationTemplateRepository.findById(id)
                .orElseThrow(()->new MyResourceNotFoundException("Notification Template not found with code: "+id));
        notificationTemplateRepository.delete(notificationTemplate);
    }
}
