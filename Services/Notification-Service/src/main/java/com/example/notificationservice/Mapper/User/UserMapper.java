package com.example.notificationservice.Mapper.User;

import com.example.notificationservice.Dto.User.UserResponse;
import com.example.notificationservice.Entity.User;

public interface UserMapper {
    UserResponse DtoFromEntity(User user);
}
