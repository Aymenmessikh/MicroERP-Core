package com.example.adminservice.Services;

import com.example.adminservice.Config.Exceptions.MyResourceNotFoundException;
import com.example.adminservice.Config.Exceptions.RessourceAlreadyDisabledException;
import com.example.adminservice.Config.Exceptions.RessourceAlreadyEnabledException;
import com.example.adminservice.Config.filter.clause.Clause;
import com.example.adminservice.Config.filter.specification.GenericSpecification;
import com.example.adminservice.Dto.Groupe.GroupeRequest;
import com.example.adminservice.Dto.Groupe.GroupeResponse;
import com.example.adminservice.Entity.Groupe;
import com.example.adminservice.Entity.Module;
import com.example.adminservice.Entity.Profile;
import com.example.adminservice.Mapper.Groupe.GroupeMapper;
import com.example.adminservice.Repository.GroupeRepository;
import com.example.adminservice.Repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupeService {
    private final GroupeRepository groupeRepository;
    private final ProfileRepository profileRepository;
    private final GroupeMapper groupeMapper;

    public GroupeResponse createGroupe(GroupeRequest groupeRequest) {
        Groupe groupe = groupeRepository.save(groupeMapper.EnityFromDto(groupeRequest));
        return groupeMapper.DtoFromEntity(groupe);
    }

    public List<GroupeResponse> getAllGroupes() {
        return groupeRepository.findAll().stream().map(groupeMapper::DtoFromEntity).collect(Collectors.toList());
    }

    public PageImpl<GroupeResponse> getAllGroupesWithFilter(List<Clause> filter, PageRequest pageRequest) {
        Specification<Groupe> specification = new GenericSpecification<>(filter);

        Page<Groupe> page = groupeRepository.findAll(specification, pageRequest);

        List<GroupeResponse> groupeResponses = page.getContent().stream()
                .map(groupeMapper::DtoFromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(groupeResponses, pageRequest, page.getTotalElements());
    }

    public GroupeResponse getGroupeById(Long id) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Groupe not found with id: " + id));
        return groupeMapper.DtoFromEntity(groupe);
    }

    public GroupeResponse getGroupeByLibelle(String libelle) {
        Groupe groupe = groupeRepository.findByLibelle(libelle)
                .orElseThrow(() -> new MyResourceNotFoundException("Groupe not found with id: " + libelle));
        return groupeMapper.DtoFromEntity(groupe);
    }

    public GroupeResponse updateGroupe(GroupeRequest groupeRequest, Long id) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Groupe not found with id: " + id));
        groupe.setLibelle(groupeRequest.getLibelle());
        Groupe groupe1 = groupeRepository.save(groupe);
        return groupeMapper.DtoFromEntity(groupe1);
    }

    public void deleteGroupe(Long id) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Groupe not found with id: " + id));
        List<Profile> profiles = profileRepository.findAllByGroupe(groupe);
        for (Profile profile : profiles) {
            profile.setGroupe(null);
            profileRepository.save(profile);
        }
        groupeRepository.delete(groupe);
    }

    public GroupeResponse enableGroupe(Long id) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Groupe not found with id: " + id));
        if (!groupe.getActif()) {
            groupe.setActif(true);
            groupeRepository.save(groupe);
        } else throw new RessourceAlreadyEnabledException("Module with ID " + id + " is already enabled");
        return groupeMapper.DtoFromEntity(groupe);
    }

    public GroupeResponse disableGroupe(Long id) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Groupe not found with id: " + id));
        if (groupe.getActif()) {
            groupe.setActif(false);
            groupeRepository.save(groupe);
            return groupeMapper.DtoFromEntity(groupe);
        } else
            throw new RessourceAlreadyDisabledException("Module with ID " + id + " is already disabled");
    }

    public Long count() {
        return groupeRepository.count();
    }
}
