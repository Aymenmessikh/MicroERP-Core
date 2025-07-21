package com.example.adminservice.Dto.AuthorityType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityTypeRequest {
    @NotNull(message = "Authority Type Libelle cannot be null")
    private String libelle;
}
