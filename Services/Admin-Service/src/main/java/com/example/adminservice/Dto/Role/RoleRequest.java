package com.example.adminservice.Dto.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRequest {
    @NotNull(message = "Role Libelle cannot be null")
    private String libelle;
    @NotNull(message = "Module cannot be null")
    private Long moduleId;
}
