package com.example.adminservice.Dto.AuthorityType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorityTypeResponse {
    private Long id;
    private String libelle;
    private Boolean actif;
}
