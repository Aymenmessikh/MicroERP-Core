package com.example.adminservice.Dto.Authority;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorityRequest {
    @NotNull(message = "Authority Libelle cannot be null")
    private String libelle;
    @NotNull(message = "Authority Type cannot be null")
    private Long authorityTypeId;
    @NotNull(message = "Module cannot be null")
    private Long moduleId;
}
