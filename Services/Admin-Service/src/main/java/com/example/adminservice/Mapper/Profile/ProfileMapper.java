package com.example.adminservice.Mapper.Profile;

import com.example.adminservice.Dto.Profile.ProfileAuthorityResponse;
import com.example.adminservice.Dto.Profile.ProfileForModuleResponse;
import com.example.adminservice.Dto.Profile.ProfileRequest;
import com.example.adminservice.Dto.Profile.ProfileResponse;
import com.example.adminservice.Entity.Profile;
import com.example.adminservice.Entity.ProfileAuthority;

public interface ProfileMapper {
    Profile EntityFromDto(ProfileRequest profileRequest);

    ProfileResponse DtoFromEntity(Profile profile);

    ProfileForModuleResponse ProfileForModuleDtoFromEntity(Profile profile);

    ProfileAuthorityResponse profileAuthorityToResponse(ProfileAuthority profileAuthority);
}
