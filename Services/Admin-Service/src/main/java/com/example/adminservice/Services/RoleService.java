package com.example.adminservice.Services;

import com.example.adminservice.Config.Exceptions.*;
import com.example.adminservice.Config.filter.clause.Clause;
import com.example.adminservice.Config.filter.specification.GenericSpecification;
import com.example.adminservice.Dto.Role.RoleRequest;
import com.example.adminservice.Dto.Role.RoleResponse;
import com.example.adminservice.Entity.Authority;
import com.example.adminservice.Entity.Module;
import com.example.adminservice.Entity.Profile;
import com.example.adminservice.Entity.Role;
import com.example.adminservice.Mapper.Role.RoleMapper;
import com.example.adminservice.Repository.AuthorityRepository;
import com.example.adminservice.Repository.ModuleRepository;
import com.example.adminservice.Repository.ProfileRepository;
import com.example.adminservice.Repository.RoleRepository;
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
public class RoleService {
    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;
    private final AuthorityRepository authorityRepository;
    private final ProfileRepository profileRepository;
    private final RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = roleRepository.save(roleMapper.EntityFromDto(roleRequest));
        return roleMapper.DtoFromEntity(role);
    }

    //    public List<RoleResponse> getAllRoles(){
//        return roleRepository.findAll().stream().map(roleMapper::DtoFromEntity).collect(Collectors.toList());
//    }
    public PageImpl<RoleResponse> getAllRoles(List<Clause> filter, PageRequest pageRequest) {
        Specification<Role> specification = new GenericSpecification<>(filter);

        Page<Role> page = roleRepository.findAll(specification, pageRequest);

        List<RoleResponse> roleResponses = page.getContent().stream()
                .map(roleMapper::DtoFromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(roleResponses, pageRequest, page.getTotalElements());
    }

    public RoleResponse getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id: " + id));
        return roleMapper.DtoFromEntity(role);
    }

    public List<RoleResponse> getAllRoleByModule(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id : " + id));
        List<Role> roles = roleRepository.findRolesByModule(module);
        if (roles != null) {
            return roles.stream().map(roleMapper::DtoFromEntity).collect(Collectors.toList());
        } else
            throw new MyResourceNotFoundException("No roles found for module with id:" + id);
    }

    public RoleResponse updateRole(RoleRequest roleRequest, Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id: " + id));
        Module module = moduleRepository.findById(roleRequest.getModuleId())
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + roleRequest.getModuleId()));
        role.setLibelle(roleRequest.getLibelle());
        role.setModule(module);
        Role role1 = roleRepository.save(role);
        return roleMapper.DtoFromEntity(role1);
    }

    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id: " + id));
        roleRepository.delete(role);
    }

    public RoleResponse enableRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id: " + id));
        if (role.getActif() == false) {
            role.setActif(true);
            roleRepository.save(role);
            return roleMapper.DtoFromEntity(role);
        } else
            throw new RessourceAlreadyEnabledException("Role with ID " + id + " is already enabled");
    }

    public RoleResponse disableRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id: " + id));
        if (role.getActif() == true) {
            role.setActif(false);
            roleRepository.save(role);
            return roleMapper.DtoFromEntity(role);
        } else
            throw new RessourceAlreadyEnabledException("Role with ID " + id + " is already disabled");
    }

    public RoleResponse grantAuthoritiesToRole(Long roleId, List<Long> authorityIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id: " + roleId));

        for (Long authorityId : authorityIds) {
            Authority authority = authorityRepository.findById(authorityId)
                    .orElseThrow(() -> new MyResourceNotFoundException("Authority not found with id: " + authorityId));

            if (role.getModule().getId().equals(authority.getModule().getId())) {
                if (!role.getAuthoritys().contains(authority)) {
                    role.grantAuthority(authority);
                } else {
                    throw new AuthorityAlreadyExistsException("Authority with id " + authorityId + " already exists for this role.");
                }
            } else {
                throw new ModuleMismatchException("Role and Authority do not belong to the same module.");
            }
        }

        roleRepository.save(role);
        return roleMapper.DtoFromEntity(role);
    }

    public RoleResponse revokeAuthoritiesFromRole(Long roleId, List<Long> authorityIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id: " + roleId));

        for (Long authorityId : authorityIds) {
            Authority authority = authorityRepository.findById(authorityId)
                    .orElseThrow(() -> new MyResourceNotFoundException("Authority not found with id: " + authorityId));

            if (role.getAuthoritys().contains(authority)) {
                role.revokeAuthority(authority);
            } else {
                throw new AuthorityNoteExistsException("Authority with id " + authorityId + " not found in the role.");
            }
        }

        roleRepository.save(role);
        return roleMapper.DtoFromEntity(role);
    }

    public List<RoleResponse> getModulesRolesExcludingProfileRole(Long idProfile) {
        Profile profile = profileRepository.findById(idProfile)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id: " + idProfile));
        List<Role> rolesProfile = profile.getRoles();
        List<Module> modulesProfile = profile.getModules();
        List<Role> rolesModules = roleRepository.findByModuleIn(modulesProfile);
        Set<Long> roleProfileIds = rolesProfile.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
        List<Role> filteredRoles = rolesModules.stream()
                .filter(role -> !roleProfileIds.contains(role.getId())) // Fix typo here
                .collect(Collectors.toList());

        return filteredRoles.stream()
                .map(roleMapper::DtoFromEntity)
                .collect(Collectors.toList());
    }
    public Long count(){
        return roleRepository.count();
    }
    public Long countByModule(Long id){
        Module module=moduleRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + id));
        return roleRepository.countByModule(module);
    }
}
