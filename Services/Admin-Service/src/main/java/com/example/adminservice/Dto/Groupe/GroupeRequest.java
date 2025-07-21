package com.example.adminservice.Dto.Groupe;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupeRequest {
    @NotNull(message = "Groupe Libelle cannot be null")
    private String libelle;
}
