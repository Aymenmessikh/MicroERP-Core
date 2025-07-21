package com.example.adminservice.Mapper.Profile;

import com.example.adminservice.Config.Exceptions.MyResourceNotFoundException;
import com.example.adminservice.Dto.Groupe.GroupeResponse;
import com.example.adminservice.Dto.Module.ModuleResponse;
import com.example.adminservice.Dto.Profile.ProfileAuthorityResponse;
import com.example.adminservice.Dto.Profile.ProfileForModuleResponse;
import com.example.adminservice.Dto.Profile.ProfileRequest;
import com.example.adminservice.Dto.Profile.ProfileResponse;
import com.example.adminservice.Dto.Role.RoleResponse;
import com.example.adminservice.Entity.Groupe;
import com.example.adminservice.Entity.Profile;
import com.example.adminservice.Entity.ProfileAuthority;
import com.example.adminservice.Entity.User;
import com.example.adminservice.Mapper.Authority.AuthorityMapper;
import com.example.adminservice.Mapper.Groupe.GroupeMapper;
import com.example.adminservice.Mapper.Module.ModuleMapper;
import com.example.adminservice.Mapper.Role.RoleMapper;
import com.example.adminservice.Repository.GroupeRepository;
import com.example.adminservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ProfileMapperImp implements ProfileMapper {
    private final GroupeRepository groupeRepository;
    private final UserRepository userRepository;
    private final GroupeMapper groupeMapper;
    private final ModuleMapper moduleMapper;
    private final RoleMapper roleMapper;
    private final AuthorityMapper authorityMapper;

    @Override
    public Profile EntityFromDto(ProfileRequest profileRequest) {
        Groupe groupe = groupeRepository.findById(profileRequest.getGroupId())
                .orElseThrow(() -> new MyResourceNotFoundException("Groupe not found with id:" + profileRequest.getGroupId()));
        User user = userRepository.findById(profileRequest.getUserId())
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with id:" + profileRequest.getUserId()));
        return Profile.builder()
                .actif(true)
                .groupe(groupe)
                .user(user)
                .libelle(profileRequest.getLibelle())
                .build();
    }

    @Override
    public ProfileResponse DtoFromEntity(Profile profile) {
        List<RoleResponse> roleResponses = new ArrayList<>();
        List<ProfileAuthorityResponse> profileAuthorityResponses = new ArrayList<>();
        List<ModuleResponse> moduleResponses = new ArrayList<>();
        GroupeResponse groupeResponse = new GroupeResponse();
        if (profile.getRoles() != null) {
            roleResponses = profile.getRoles().stream()
                    .map(roleMapper::DtoFromEntity).collect(Collectors.toList());
        }
        if (profile.getProfileAuthorities() != null) {
            profileAuthorityResponses = profile.getProfileAuthorities().stream()
                    .map(this::profileAuthorityToResponse).collect(Collectors.toList());
        }
        if (profile.getModules() != null) {
            moduleResponses = profile.getModules().stream()
                    .map(moduleMapper::DtoFromEntity).collect(Collectors.toList());
        }
        if (profile.getGroupe() != null) {
            groupeResponse = groupeMapper.DtoFromEntity(profile.getGroupe());
        }
        return ProfileResponse.builder()
                .actif(profile.getActif())
                .groupeResponse(groupeResponse)
                .id(profile.getId())
                .libelle(profile.getLibelle())
                .moduleResponses(moduleResponses)
                .roleResponses(roleResponses)
                .profileAuthorityResponses(profileAuthorityResponses)
                .build();

    }

    @Override
    public ProfileForModuleResponse ProfileForModuleDtoFromEntity(Profile profile) {
        return ProfileForModuleResponse.builder()
                .id(profile.getId())
                .libelle(profile.getLibelle())
                .build();
    }

    @Override
    public ProfileAuthorityResponse profileAuthorityToResponse(ProfileAuthority profileAuthority) {
        return ProfileAuthorityResponse.builder()
                .id(profileAuthority.getId())
                .authorityResponse(authorityMapper.DtoFromEntity(profileAuthority.getAuthority()))
                .granted(profileAuthority.getGranted())
                .build();
    }
}
