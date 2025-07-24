package com.example.notificationservice.Mapper.User;

import com.example.notificationservice.Dto.User.UserResponse;
import com.example.notificationservice.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImp implements UserMapper{
    @Override
    public UserResponse DtoFromEntity(User user) {
        return UserResponse.builder()
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .uuid(user.getUuid())
                .id(user.getId())
                .build();
    }
}
