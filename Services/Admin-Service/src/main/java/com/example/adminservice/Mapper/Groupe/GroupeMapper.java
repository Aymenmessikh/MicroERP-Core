package com.example.adminservice.Mapper.Groupe;

import com.example.adminservice.Dto.Groupe.GroupeRequest;
import com.example.adminservice.Dto.Groupe.GroupeResponse;
import com.example.adminservice.Entity.Groupe;

public interface GroupeMapper {
    Groupe EnityFromDto(GroupeRequest groupeRequest);

    GroupeResponse DtoFromEntity(Groupe groupe);
}
