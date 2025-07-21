package com.example.adminservice.Services;

import com.example.adminservice.Config.Exceptions.MyResourceNotFoundException;
import com.example.adminservice.Entity.Authority;
import com.example.adminservice.Entity.Module;
import com.example.adminservice.Entity.Profile;
import com.example.adminservice.Repository.AuthorityRepository;
import com.example.adminservice.Repository.ModuleRepository;
import com.example.adminservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermisionService {
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    //    public Boolean verfierPermission(String authorityLibelle, String moduleCode, String userName) {
//        Module module = moduleRepository.findByModuleCode(moduleCode)
//                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with code: " + moduleCode));
//        Authority authority = authorityRepository.findByLibelleAndModule(authorityLibelle, module)
//                .orElseThrow(() -> new MyResourceNotFoundException("Authority not found with libelle: " + authorityLibelle));
//        User user = userRepository.findByUserName(userName)
//                .orElseThrow(() -> new MyResourceNotFoundException("User not found with username: " + userName));
//        Profile activeProfile = user.getActifProfile();
//        if (activeProfile.getModules().contains(module)) {
//            List<Authority> authorities = activeProfile.getProfileAuthorities()
//                    .stream().filter(profileAuthority -> profileAuthority.getGranted())
//                    .map(profileAuthority -> profileAuthority.getAuthority())
//                    .filter(auth -> auth.getModule().equals(module))
//                    .collect(Collectors.toList());
//            List<Role> roles = activeProfile.getRoles()
//                    .stream().filter(role -> role.getModule().equals(module)).collect(Collectors.toList());
//            List<Authority> authorities1 = roles.stream()
//                    .flatMap(role -> role.getAuthoritys().stream())
//                    .collect(Collectors.toList());
//            Set<Authority> authoritySet = new HashSet<>();
//            authoritySet.addAll(authorities1);
//            authoritySet.addAll(authorities);
//            if (authoritySet.contains(authority)){
//                return true;
//            }else return false;
//        } else {
//            return false;
//        }
//    }
//    public Boolean getPermissionForUser(String authorityLibelle, String moduleCode) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) authentication;
//        Jwt jwt = jwtAuthentication.getToken();
//
//        String username = (String) jwt.getClaims().get("preferred_username");
//
//        /* get authority by libelle and module name */
//
//        Module module = moduleRepository.findByModuleCode(moduleCode)
//                .orElseThrow(() -> new MyResourceNotFoundException("module not found"));
//        Authority authority = authorityRepository.findByLibelleAndModule(authorityLibelle, module)
//                .orElseThrow(() -> new MyResourceNotFoundException("authority notFound"));
//
//        /* get user actif profile authorities and check if conatins the authority requested */
//        Profile profile = userRepository.findByUserName(username)
//                .orElseThrow(() -> new MyResourceNotFoundException("user not found")).getActifProfile();
//
//        /* return false for revoked authority or true if granted */
//        if (profile.getProfileAuthorities().stream().anyMatch(profileAuthority -> profileAuthority.getAuthority().equals(authority))) {
//            return profile.getProfileAuthorities().stream()
//                    .anyMatch(profileAuthority -> profileAuthority.getAuthority().equals(authority) && profileAuthority.getGranted());
//        }
//
//        if (profile != null) {
//            boolean hasMatchingAuth = profile.getRoles().stream().flatMap(role -> role.getAuthoritys().stream())
//                    .anyMatch(auth -> auth.equals(authority));
//
//            boolean hasModule = profile.getModules().contains(module);
//
//            return hasMatchingAuth && hasModule;
//        } else {
//            return false;
//        }
//    }
    public Boolean getPermissionForUser(String authorityLibelle, String moduleCode,String username) {


        Module module = moduleRepository.findByModuleCode(moduleCode)
                .orElseThrow(() -> new MyResourceNotFoundException("module not found"));
        Authority authority = authorityRepository.findByLibelleAndModule(authorityLibelle, module)
                .orElseThrow(() -> new MyResourceNotFoundException("authority notFound"));

        Profile profile = userRepository.findByUserName(username)
                .orElseThrow(() -> new MyResourceNotFoundException("user not found")).getActifProfile();

        /* return false for revoked authority or true if granted */
        if (profile.getProfileAuthorities().stream().anyMatch(profileAuthority -> profileAuthority.getAuthority().equals(authority))) {
            return profile.getProfileAuthorities().stream()
                    .anyMatch(profileAuthority -> profileAuthority.getAuthority().equals(authority) && profileAuthority.getGranted());
        }

        if (profile != null) {
            boolean hasMatchingAuth = profile.getRoles().stream().flatMap(role -> role.getAuthoritys().stream())
                    .anyMatch(auth -> auth.equals(authority));

            boolean hasModule = profile.getModules().contains(module);

            return hasMatchingAuth && hasModule;
        } else {
            return false;
        }
    }
}