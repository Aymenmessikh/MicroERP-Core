package com.example.adminservice.Mapper.Authority;

import com.example.adminservice.Dto.Authority.AuthorityRequest;
import com.example.adminservice.Dto.Authority.AuthorityResponse;
import com.example.adminservice.Entity.Authority;
import com.example.adminservice.Entity.ProfileAuthority;

public interface AuthorityMapper {
    Authority EntityFromDto(AuthorityRequest authorityRequest);

    AuthorityResponse DtoFromEntity(Authority authority);

    AuthorityResponse DtoFromProfileAuthorityEntity(ProfileAuthority profileAuthority);
}
