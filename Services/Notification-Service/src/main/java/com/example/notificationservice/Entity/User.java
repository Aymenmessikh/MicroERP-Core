package com.example.notificationservice.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "_user")
public class User implements Serializable {
    @Id
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @NotBlank
    @Column(unique = true,nullable = false)
    private String userName;
    @Column(nullable = false)
    private String email;
    @NotBlank
    @Column(nullable = false)
    private String uuid;
    private String phoneNumber;
}
