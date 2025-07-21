package com.example.adminservice.Mapper.Authority;

import com.example.adminservice.Config.Exceptions.MyResourceNotFoundException;
import com.example.adminservice.Dto.Authority.AuthorityRequest;
import com.example.adminservice.Dto.Authority.AuthorityResponse;
import com.example.adminservice.Entity.Authority;
import com.example.adminservice.Entity.AuthorityType;
import com.example.adminservice.Entity.Module;
import com.example.adminservice.Entity.ProfileAuthority;
import com.example.adminservice.Mapper.AuthorityType.AuthorityTypeMapper;
import com.example.adminservice.Mapper.Module.ModuleMapper;
import com.example.adminservice.Repository.AuthorityTypeRepository;
import com.example.adminservice.Repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorityMapperImp implements AuthorityMapper {
    private final AuthorityTypeRepository authorityTypeRepository;
    private final ModuleRepository moduleRepository;
    private final AuthorityTypeMapper authorityTypeMapper;
    private final ModuleMapper moduleMapper;

    @Override
    public Authority EntityFromDto(AuthorityRequest authorityRequest) {
        AuthorityType authorityType = authorityTypeRepository
                .findById(authorityRequest.getAuthorityTypeId())
                .orElseThrow(() -> new MyResourceNotFoundException
                        ("Authority Type not found with id: " + authorityRequest.getAuthorityTypeId()));
        Module module = moduleRepository.findById(authorityRequest.getModuleId())
                .orElseThrow(() -> new MyResourceNotFoundException
                        ("Module not found with id:" + authorityRequest.getModuleId()));
        return Authority.builder()
                .libelle(authorityRequest.getLibelle())
                .actif(true)
                .module(module)
                .authorityType(authorityType)
                .build();
    }

    @Override
    public AuthorityResponse DtoFromEntity(Authority authority) {
        return AuthorityResponse.builder()
                .id(authority.getId())
                .actif(authority.getActif())
                .libelle(authority.getLibelle())
                .module(moduleMapper.DtoFromEntity(authority.getModule()))
                .authorityType(authorityTypeMapper.DtoFromEntity(authority.getAuthorityType()))
                .build();
    }

    @Override
    public AuthorityResponse DtoFromProfileAuthorityEntity(ProfileAuthority profileAuthority) {
        return AuthorityResponse.builder()
                .libelle(profileAuthority.getAuthority().getLibelle())

                .build();
    }
}
