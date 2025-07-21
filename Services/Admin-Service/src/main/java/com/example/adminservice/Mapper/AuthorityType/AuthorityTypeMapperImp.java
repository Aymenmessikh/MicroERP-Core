package com.example.adminservice.Mapper.AuthorityType;

import com.example.adminservice.Dto.AuthorityType.AuthorityTypeRequest;
import com.example.adminservice.Dto.AuthorityType.AuthorityTypeResponse;
import com.example.adminservice.Entity.AuthorityType;
import org.springframework.stereotype.Component;

@Component
public class AuthorityTypeMapperImp implements AuthorityTypeMapper {
    @Override
    public AuthorityType EntityFromDto(AuthorityTypeRequest authorityTypeRequest) {
        return com.example.adminservice.Entity.AuthorityType.builder()
                .libelle(authorityTypeRequest.getLibelle())
                .actif(true)
                .build();
    }

    @Override
    public AuthorityTypeResponse DtoFromEntity(AuthorityType authorityType) {
        return AuthorityTypeResponse.builder()
                .id(authorityType.getId())
                .libelle(authorityType.getLibelle())
                .actif(authorityType.getActif())
                .build();
    }
}
