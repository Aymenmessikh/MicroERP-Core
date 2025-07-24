package com.example.notificationservice.Listner.Modele;

import com.example.notificationservice.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phoneNumber;
    private String uuid;
    public static User EntityFromUserListner(UserEvent userEvent){
        return User.builder()
                .userName(userEvent.getUserName())
                .email(userEvent.getEmail())
                .firstName(userEvent.getFirstName())
                .lastName(userEvent.getLastName())
                .phoneNumber(userEvent.getPhoneNumber())
                .id(userEvent.getId())
                .uuid(userEvent.getUuid())
                .build();
    }

}
