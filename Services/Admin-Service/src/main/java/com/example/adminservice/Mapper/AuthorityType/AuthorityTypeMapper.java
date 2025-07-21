package com.example.adminservice.Mapper.AuthorityType;

import com.example.adminservice.Dto.AuthorityType.AuthorityTypeRequest;
import com.example.adminservice.Dto.AuthorityType.AuthorityTypeResponse;
import com.example.adminservice.Entity.AuthorityType;

public interface AuthorityTypeMapper {
    AuthorityType EntityFromDto(AuthorityTypeRequest authorityTypeRequest);

    AuthorityTypeResponse DtoFromEntity(AuthorityType authorityType);
}
