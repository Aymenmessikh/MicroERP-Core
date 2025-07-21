package com.example.adminservice.Dto.User;

public interface UserProfileInfoDto {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getPhoneNumber();
    String getLibelle(); // profile label
}

