package com.example.adminservice.Services;

import com.example.adminservice.Config.Exceptions.MyResourceNotFoundException;
import com.example.adminservice.Config.Exceptions.RessourceAlreadyEnabledException;
import com.example.adminservice.Dto.AuthorityType.AuthorityTypeRequest;
import com.example.adminservice.Dto.AuthorityType.AuthorityTypeResponse;
import com.example.adminservice.Entity.AuthorityType;
import com.example.adminservice.Mapper.AuthorityType.AuthorityTypeMapper;
import com.example.adminservice.Repository.AuthorityTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorityTypeService {
    private final AuthorityTypeRepository authorityTypeRepository;
    private final AuthorityTypeMapper authorityTypeMapper;

    public AuthorityTypeResponse createAuthorityType(AuthorityTypeRequest authorityTypeRequest) {
        AuthorityType authorityType = authorityTypeRepository.save(authorityTypeMapper.EntityFromDto(authorityTypeRequest));
        return authorityTypeMapper.DtoFromEntity(authorityType);
    }

    public List<AuthorityTypeResponse> getAllAuthoritysType() {
        return authorityTypeRepository.findAll().stream().map(authorityTypeMapper::DtoFromEntity).collect(Collectors.toList());
    }

    public AuthorityTypeResponse getAuthorityTypeById(Long id) {
        AuthorityType authorityType = authorityTypeRepository
                .findById(id).orElseThrow(() -> new MyResourceNotFoundException("Authority Type not found with id:" + id));
        return authorityTypeMapper.DtoFromEntity(authorityType);
    }

    public AuthorityTypeResponse updateAuthorityType(AuthorityTypeRequest authorityTypeRequest, Long id) {
        AuthorityType authorityType = authorityTypeRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("AuthorityType not found with id: " + id));
        authorityType.setLibelle(authorityTypeRequest.getLibelle());
        AuthorityType authorityType1 = authorityTypeRepository.save(authorityType);
        return authorityTypeMapper.DtoFromEntity(authorityType1);
    }

    public void deleteAuthorityType(Long id) {
        AuthorityType authorityType = authorityTypeRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("AuthorityType not found with id: " + id));
        authorityTypeRepository.delete(authorityType);
    }

    public AuthorityTypeResponse enableAuthorityType(Long id) {
        AuthorityType authorityType = authorityTypeRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("AuthorityType not found with id: " + id));
        if (authorityType.getActif() == false) {
            authorityType.setActif(true);
            authorityTypeRepository.save(authorityType);
            return authorityTypeMapper.DtoFromEntity(authorityType);
        } else
            throw new RessourceAlreadyEnabledException("AuthorityType with ID " + id + " is already enabled");
    }

    public AuthorityTypeResponse disableAuthorityType(Long id) {
        AuthorityType authorityType = authorityTypeRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("AuthorityT ype not found with id: " + id));
        if (authorityType.getActif() == true) {
            authorityType.setActif(false);
            authorityTypeRepository.save(authorityType);
            return authorityTypeMapper.DtoFromEntity(authorityType);
        } else
            throw new RessourceAlreadyEnabledException("Authority Type with ID " + id + " is already disabled");
    }
    public Long count(){
        return authorityTypeRepository.count();
    }
}
