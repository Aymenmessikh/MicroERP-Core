package com.example.adminservice.Events.Modele;

import com.example.adminservice.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEvent {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phoneNumber;
    private String uuid;
    public static UserEvent UserEventFromEntity(User user){
        return UserEvent.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .id(user.getId())
                .uuid(user.getUuid())
                .build();
    }

}
