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
public class NotificationTemplate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false,unique = true)
    private String templateCode;
    @NotBlank
    @Column(nullable = false)
    private String subject;
    @NotBlank
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Boolean active;
}
