package com.example.adminservice.Dto.Profile;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileRequest {
    @NotNull(message = "Libelle cannot be null")
    private String libelle;
    private Long userId;
    private Long groupId;
}
