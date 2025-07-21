package com.example.adminservice.Mapper.Groupe;

import com.example.adminservice.Dto.Groupe.GroupeRequest;
import com.example.adminservice.Dto.Groupe.GroupeResponse;
import com.example.adminservice.Entity.Groupe;
import org.springframework.stereotype.Component;

@Component
public class GroupeMapperImp implements GroupeMapper {
    @Override
    public Groupe EnityFromDto(GroupeRequest groupeRequest) {
        return Groupe.builder()
                .libelle(groupeRequest.getLibelle())
                .actif(true)
                .build();
    }

    @Override
    public GroupeResponse DtoFromEntity(Groupe groupe) {
        return GroupeResponse.builder()
                .id(groupe.getId())
                .libelle(groupe.getLibelle())
                .actif(groupe.getActif())
                .build();
    }
}
