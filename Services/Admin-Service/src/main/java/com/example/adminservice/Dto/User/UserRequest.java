package com.example.adminservice.Dto.User;
import com.example.adminservice.Entity.Profile;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @NotNull(message = "First Name cannot be null")
    private String firstName;
    @NotNull(message = "Last Name cannot be null")
    private String lastName;
    @NotNull(message = "User Name cannot be null")
    private String userName;
    @NotNull(message = "Email cannot be null")
    private String email;
//    @NotNull(message = "Password cannot be null")
//    private String password;
    private String address;
    private String phoneNumber;
    private Profile actifProfile;
}
