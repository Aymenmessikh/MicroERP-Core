package com.example.adminservice.Dto.User;

import com.example.adminservice.Dto.Profile.ProfileResponse;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String uuid;
    private String phoneNumber;
    private Boolean actif;
    private List<ProfileResponse> profiles;
    private ProfileResponse actifProfile;
}
