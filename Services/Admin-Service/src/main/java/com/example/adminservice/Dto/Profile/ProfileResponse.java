package com.example.adminservice.Dto.Profile;

import com.example.adminservice.Dto.Groupe.GroupeResponse;
import com.example.adminservice.Dto.Module.ModuleResponse;
import com.example.adminservice.Dto.Role.RoleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    private Long id;
    private String libelle;
    private Boolean actif;
    //    private UserResponse userResponse;
    private GroupeResponse groupeResponse;
    private List<ModuleResponse> moduleResponses;
    private List<RoleResponse> roleResponses;
    private List<ProfileAuthorityResponse> profileAuthorityResponses;
}
