package com.example.adminservice.Mapper.User;

import com.example.adminservice.Dto.User.UserRequest;
import com.example.adminservice.Dto.User.UserResponse;
import com.example.adminservice.Entity.User;

public interface UserMapper {
    User EntityFromDto(UserRequest userRequest);

    UserResponse DtoFromAllEntity(User user);

    UserResponse DtoFromEntity(User user);
}
