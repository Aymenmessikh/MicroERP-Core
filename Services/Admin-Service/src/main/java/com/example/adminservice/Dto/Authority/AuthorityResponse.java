package com.example.adminservice.Dto.Authority;
import com.example.adminservice.Dto.AuthorityType.AuthorityTypeResponse;
import com.example.adminservice.Dto.Module.ModuleResponse;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorityResponse {
    private Long id;
    private String libelle;
    private Boolean actif;
    private ModuleResponse module;
    private AuthorityTypeResponse authorityType;
}
