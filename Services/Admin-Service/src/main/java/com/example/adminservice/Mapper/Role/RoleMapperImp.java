package com.example.adminservice.Mapper.Role;

import com.example.adminservice.Config.Exceptions.MyResourceNotFoundException;
import com.example.adminservice.Dto.Authority.AuthorityResponse;
import com.example.adminservice.Dto.Role.RoleRequest;
import com.example.adminservice.Dto.Role.RoleResponse;
import com.example.adminservice.Entity.Authority;
import com.example.adminservice.Entity.Module;
import com.example.adminservice.Entity.Role;
import com.example.adminservice.Mapper.Authority.AuthorityMapper;
import com.example.adminservice.Mapper.Module.ModuleMapper;
import com.example.adminservice.Repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleMapperImp implements RoleMapper {
    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;
    private final AuthorityMapper authorityMapper;

    @Override
    public Role EntityFromDto(RoleRequest roleRequest) {
        Module module = moduleRepository.findById(roleRequest.getModuleId())
                .orElseThrow(() -> new MyResourceNotFoundException("Authority Type not found with id: " + roleRequest.getModuleId()));
        return Role.builder()
                .libelle(roleRequest.getLibelle())
                .actif(true)
                .module(module)
                .build();
    }

    @Override
    public RoleResponse DtoFromEntity(Role role) {
        List<Authority> authoritys = role.getAuthoritys();
        if (authoritys == null) {
            return RoleResponse.builder()
                    .id(role.getId())
                    .actif(role.getActif())
                    .libelle(role.getLibelle())
                    .module(moduleMapper.DtoFromEntity(role.getModule()))
                    .build();
        } else {
            List<AuthorityResponse> authorityResponses = authoritys.stream()
                    .map(authorityMapper::DtoFromEntity).collect(Collectors.toList());
            return RoleResponse.builder()
                    .id(role.getId())
                    .actif(role.getActif())
                    .libelle(role.getLibelle())
                    .module(moduleMapper.DtoFromEntity(role.getModule()))
                    .authoritys(authorityResponses)
                    .build();
        }
    }
}
