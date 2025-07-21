package com.example.adminservice.Dto.Role;

import com.example.adminservice.Dto.Authority.AuthorityResponse;
import com.example.adminservice.Dto.Module.ModuleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {
    private Long id;
    private String libelle;
    private Boolean actif;
    private List<AuthorityResponse> authoritys;
    private ModuleResponse module;
}
