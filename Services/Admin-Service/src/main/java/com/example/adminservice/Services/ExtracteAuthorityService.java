package com.example.adminservice.Services;

import com.example.adminservice.Entity.Authority;
import com.example.adminservice.Entity.Profile;
import com.example.adminservice.Entity.Role;
import com.example.adminservice.Repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

;

@Service
@RequiredArgsConstructor
@Transactional
public class ExtracteAuthorityService {
    private final ProfileRepository profileRepository;
    @Value("${module_name}")
    private String MODULE_NAME;

    public Profile fetchProfileWithAuthorities(Long profileId) {
        return profileRepository.findById(profileId)
                .map(profile -> {
                    profile.getProfileAuthorities().size();
                    profile.getRoles().forEach(role -> role.getAuthoritys().size());
                    return profile;
                }).orElse(null);
    }

    public Set<GrantedAuthority> extractAuthority(Long id) {
        Profile activeProfile = fetchProfileWithAuthorities(id);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        if (activeProfile.getProfileAuthorities() != null) {
            Set<Authority> authorities = activeProfile.getProfileAuthorities()
                    .stream()
                    .map(profileAuthority -> profileAuthority.getAuthority())
                    .filter(authority -> authority.getModule().getModuleName().equals(MODULE_NAME))
                    .collect(Collectors.toSet());
            Set<GrantedAuthority> grantedAuthorities1 = authorities
                    .stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getLibelle()))
                    .collect(Collectors.toSet());
            grantedAuthorities.addAll(grantedAuthorities1);
        }

        if (activeProfile.getRoles() != null) {
            Set<Role> roles = new HashSet<>(activeProfile.getRoles());
            Set<Authority> authoritiesRoles = roles.stream()
                    .flatMap(role -> {
                        if (role.getAuthoritys() != null) {
                            return role.getAuthoritys().stream()
                                    .filter(authority -> authority.getModule().getModuleCode().equals(MODULE_NAME));
                        } else {
                            return Stream.empty();
                        }
                    })
                    .collect(Collectors.toSet());

            Set<GrantedAuthority> grantedAuthorities2 = authoritiesRoles.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getLibelle()))
                    .collect(Collectors.toSet());

            grantedAuthorities.addAll(grantedAuthorities2);
        }

        return grantedAuthorities;
    }
}
