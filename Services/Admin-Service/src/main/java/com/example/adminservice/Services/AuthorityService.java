package com.example.adminservice.Services;

import com.example.adminservice.Config.Exceptions.MyResourceNotFoundException;
import com.example.adminservice.Config.Exceptions.RessourceAlreadyEnabledException;
import com.example.adminservice.Config.filter.clause.Clause;
import com.example.adminservice.Config.filter.specification.GenericSpecification;
import com.example.adminservice.Dto.Authority.AuthorityRequest;
import com.example.adminservice.Dto.Authority.AuthorityResponse;
import com.example.adminservice.Entity.Module;
import com.example.adminservice.Entity.*;
import com.example.adminservice.Mapper.Authority.AuthorityMapper;
import com.example.adminservice.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;
    private final AuthorityTypeRepository authorityTypeRepository;
    private final ModuleRepository moduleRepository;
    private final RoleRepository roleRepository;
    private final AuthorityMapper authorityMapper;
    private final ProfileRepository profileRepository;

    public AuthorityResponse createAuthority(AuthorityRequest authorityRequest) {
        Authority authority = authorityRepository.save(authorityMapper.EntityFromDto(authorityRequest));
        return authorityMapper.DtoFromEntity(authority);
    }

    public PageImpl<AuthorityResponse> getAllAuthoritys(List<Clause> filter, PageRequest pageRequest) {
        Specification<Authority> specification = new GenericSpecification<>(filter);

        Page<Authority> page = authorityRepository.findAll(specification, pageRequest);

        List<AuthorityResponse> authorityResponses = page.getContent().stream()
                .map(authorityMapper::DtoFromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(authorityResponses, pageRequest, page.getTotalElements());
    }

    public AuthorityResponse getAuthorityById(Long id) {
        Authority authority = authorityRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Authority not found with id: " + id));
        return authorityMapper.DtoFromEntity(authority);
    }

    public List<AuthorityResponse> getAllAuthorityByModule(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id : " + id));
        List<Authority> authorities = authorityRepository.getAuthoritiesByModule(module);
        if (authorities != null) {
            return authorities.stream().map(authorityMapper::DtoFromEntity).collect(Collectors.toList());
        } else
            throw new MyResourceNotFoundException("No authority found for module with id:" + id);
    }

    public List<AuthorityResponse> getAllAuthorityByRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id : " + id));
        List<Authority> authorities = authorityRepository.getAuthoritiesByRoles(role);
        if (authorities != null) {
            return authorities.stream().map(authorityMapper::DtoFromEntity).collect(Collectors.toList());
        } else
            throw new MyResourceNotFoundException("No authority found for Role with id:" + id);
    }

    public AuthorityResponse updateAuthority(AuthorityRequest authorityRequest, Long id) {
        Authority authority = authorityRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Authority not found with id: " + id));
        AuthorityType authorityType = authorityTypeRepository.
                findById(authorityRequest.getAuthorityTypeId())
                .orElseThrow(() -> new MyResourceNotFoundException("Authority Type not found with id: " + authorityRequest.getAuthorityTypeId()));
        Module module = moduleRepository.findById(authorityRequest.getModuleId())
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + authorityRequest.getModuleId()));
        authority.setLibelle(authorityRequest.getLibelle());
        authority.setAuthorityType(authorityType);
        authority.setModule(module);
        Authority authority1 = authorityRepository.save(authority);
        return authorityMapper.DtoFromEntity(authority1);
    }

    public void deleteAuthority(Long id) {
        Authority authority = authorityRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Authority not found with id: " + id));
        authorityRepository.delete(authority);
    }

    public AuthorityResponse enableAuthority(Long id) {
        Authority authority = authorityRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Authority not found with id: " + id));
        if (authority.getActif() == false) {
            authority.setActif(true);
            authorityRepository.save(authority);
            return authorityMapper.DtoFromEntity(authority);
        } else
            throw new RessourceAlreadyEnabledException("Authority with ID " + id + " is already enabled");
    }

    public AuthorityResponse disableAuthority(Long id) {
        Authority authority = authorityRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Authority not found with id: " + id));
        if (authority.getActif() == true) {
            authority.setActif(false);
            authorityRepository.save(authority);
            return authorityMapper.DtoFromEntity(authority);
        } else
            throw new RessourceAlreadyEnabledException("Authority with ID " + id + " is already disabled");
    }

    public List<AuthorityResponse> getModuleAuthoritiesExcludingRoleAuthorities(Long idRole, Long idModule) {
        Role role = roleRepository.findById(idRole)
                .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id: " + idRole));
        Module module = moduleRepository.findById(idModule)
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + idModule));
        List<Authority> authoritiesRole = role.getAuthoritys();
        List<Authority> authoritiesModule = authorityRepository.getAuthoritiesByModule(module);
        Set<Long> roleAuthorityIds = authoritiesRole.stream()
                .map(Authority::getId) // Assuming Authority has an 'id' field
                .collect(Collectors.toSet());
        List<Authority> filteredAuthorities = authoritiesModule.stream()
                .filter(authority -> !roleAuthorityIds.contains(authority.getId()))
                .collect(Collectors.toList());
        return filteredAuthorities.stream().map(authorityMapper::DtoFromEntity).collect(Collectors.toList());
    }

    public List<AuthorityResponse> getModulesAuthoritiesExcludingProfileAuthorities(Long idProfile) {
        Profile profile = profileRepository.findById(idProfile)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id: " + idProfile));
        List<ProfileAuthority> profileAuthorities = profile.getProfileAuthorities();
        List<Authority> authoritiesProfile = profileAuthorities.stream()
                .map(ProfileAuthority::getAuthority)
                .collect(Collectors.toList());

        Set<Long> profileAuthorityIds = authoritiesProfile.stream()
                .map(Authority::getId)
                .collect(Collectors.toSet());

        List<Module> modules = profile.getModules();
        List<Authority> authoritiesModules = authorityRepository.findByModuleIn(modules);

        List<Authority> filteredAuthorities = authoritiesModules.stream()
                .filter(authority -> !profileAuthorityIds.contains(authority.getId()))
                .collect(Collectors.toList());


        return filteredAuthorities.stream().map(authorityMapper::DtoFromEntity).collect(Collectors.toList());
    }
    public Long count(){
        return authorityRepository.count();
    }
    public Long countByModule(Long id){
        Module module=moduleRepository.findById(id)
                 .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + id));
        return authorityRepository.countByModule(module);
    }

}
