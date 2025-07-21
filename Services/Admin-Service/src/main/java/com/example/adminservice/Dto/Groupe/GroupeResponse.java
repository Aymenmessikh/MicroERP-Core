package com.example.adminservice.Dto.Groupe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupeResponse {
    private Long id;
    private String libelle;
    private Boolean actif;
}
