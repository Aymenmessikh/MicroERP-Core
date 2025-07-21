package com.example.adminservice.Mapper.User;

import com.example.adminservice.Dto.Profile.ProfileResponse;
import com.example.adminservice.Dto.User.UserRequest;
import com.example.adminservice.Dto.User.UserResponse;
import com.example.adminservice.Entity.User;
import com.example.adminservice.Mapper.Profile.ProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapperImp implements UserMapper {
    private final ProfileMapper profileMapper;

    @Override
    public User EntityFromDto(UserRequest userRequest) {
        return User.builder()
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .actif(true)
                .userName(userRequest.getUserName())
                .phoneNumber(userRequest.getPhoneNumber())
                .build();
    }

    @Override
    public UserResponse DtoFromEntity(User user) {
        List<ProfileResponse> profileResponses = new ArrayList<>();
        ProfileResponse profileResponse = null;
        if (user.getProfiles() != null) {
            profileResponses = user.getProfiles()
                    .stream().map(profileMapper::DtoFromEntity).collect(Collectors.toList());
        }
        if (user.getActifProfile() != null) {
            profileResponse = profileMapper.DtoFromEntity(user.getActifProfile());
        }
        return UserResponse.builder()
                .userName(user.getUserName())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .id(user.getId())
                .actifProfile(profileResponse)
                .profiles(profileResponses)
                .actif(user.getActif())
                .email(user.getEmail())
                .uuid(user.getUuid())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Override
    public UserResponse DtoFromAllEntity(User user) {
        return UserResponse.builder()
                .userName(user.getUserName())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .id(user.getId())
                .actif(user.getActif())
                .email(user.getEmail())
                .uuid(user.getUuid())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
