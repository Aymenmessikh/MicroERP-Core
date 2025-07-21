package com.example.adminservice.Services;

import com.example.adminservice.Config.Exceptions.MyResourceNotFoundException;
import com.example.adminservice.Config.Exceptions.ProfileAlreadyActiveException;
import com.example.adminservice.Config.Exceptions.RessourceAlreadyDisabledException;
import com.example.adminservice.Config.Exceptions.RessourceAlreadyEnabledException;
import com.example.adminservice.Dto.Profile.ProfileRequest;
import com.example.adminservice.Dto.Profile.ProfileResponse;
import com.example.adminservice.Dto.User.UserResponse;
import com.example.adminservice.Entity.Module;
import com.example.adminservice.Entity.*;
import com.example.adminservice.Mapper.Profile.ProfileMapper;
import com.example.adminservice.Mapper.User.UserMapper;
import com.example.adminservice.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final GroupeRepository groupeRepository;
    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;
    private final AuthorityRepository authorityRepository;
    private final ProfileAuthorityRepository profileAuthorityRepository;
    private final ProfileMapper profileMapper;
    private final UserMapper userMapper;

    public ProfileResponse addProfileToUser(ProfileRequest profileRequest, Long profileTypeId) {
        User user = userRepository.findById(profileRequest.getUserId())
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with id: " + profileRequest.getUserId()));
        Groupe groupe = null;
        if (profileRequest.getGroupId() != null) {
            groupe = groupeRepository.findById(profileRequest.getGroupId())
                    .orElseThrow(() -> new MyResourceNotFoundException("Groupe not found with id: " + profileRequest.getGroupId()));
        }
        Profile profile;
        if (profileTypeId != -1) {
            Profile profileType = profileRepository.findById(profileTypeId)
                    .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id: " + profileTypeId));
            profile = Profile.builder()
                    .libelle(profileRequest.getLibelle())
                    .groupe(profileType.getGroupe())
                    .roles(new ArrayList<>(profileType.getRoles()))
                    .modules(new ArrayList<>(profileType.getModules()))
                    .user(user)
                    .actif(true)
                    .build();

            // Sauvegarder le profil
            Profile savedProfile = profileRepository.save(profile);

            // Créer et sauvegarder les autorités associées au profil
            List<ProfileAuthority> newProfileAuthorities = new ArrayList<>();
            for (ProfileAuthority authority : profileType.getProfileAuthorities()) {
                ProfileAuthority newAuthority = ProfileAuthority.builder()
                        .authority(authority.getAuthority())
                        .granted(authority.getGranted())
                        .profile(savedProfile) // Référence au profil sauvegardé
                        .build();
                newProfileAuthorities.add(newAuthority);
            }

            // Associer les autorités au profil sauvegardé
            savedProfile.setProfileAuthorities(newProfileAuthorities);
            profileRepository.save(savedProfile);
        } else {
            // Créer un profil basé sur la requête
            profile = Profile.builder()
                    .libelle(profileRequest.getLibelle())
                    .groupe(groupe)
                    .user(user)
                    .actif(true)
                    .build();
            profileRepository.save(profile);
        }

        // Ajouter le profil à l'utilisateur et sauvegarder l'utilisateur
        user.addProfile(profile);
        userRepository.save(user);

        // Retourner la réponse du profil
        return profileMapper.DtoFromEntity(profile);
    }

    public ProfileResponse getProfileById(Long id) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id: " + id));
        return profileMapper.DtoFromEntity(profile);
    }

    public void removeProfileFromUser(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id:" + profileId));

        User user = userRepository.findByProfiles(profile)
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with Profile:" + profile));

        if (!user.getActifProfile().equals(profile)) {
            user.removeProfile(profile);
            profileRepository.delete(profile);
        } else {
            throw new IllegalStateException("Cannot delete the active profile of the user.");
        }
    }


    public ProfileResponse updateProfile(ProfileRequest profileRequest, Long profileId) {
        Groupe groupe = null;
        if (profileRequest.getGroupId() != null) {
            groupe = groupeRepository.findById(profileRequest.getGroupId())
                    .orElseThrow(() -> new MyResourceNotFoundException("Groupe not found with id:" + profileRequest.getGroupId()));
        }
//        User user = userRepository.findById(profileRequest.getUserId())
//                .orElseThrow(() -> new MyResourceNotFoundException("User not found with id:" + profileRequest.getUserId()));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id:" + profileId));
        profile.setLibelle(profileRequest.getLibelle());
        profile.setGroupe(groupe);
        // profile.setUser(user);
        profileRepository.save(profile);
        return profileMapper.DtoFromEntity(profile);
    }

    public ProfileResponse activeProfile(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id:" + profileId));
        if (profile.getActif() == false) {
            profile.setActif(true);
            profileRepository.save(profile);
            return profileMapper.DtoFromEntity(profile);
        } else
            throw new RessourceAlreadyEnabledException("Profile Already Active");
    }

    public ProfileResponse desactiveProfile(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id:" + profileId));
        if (profile.getActif() == true) {
            profile.setActif(false);
            profileRepository.save(profile);
            return profileMapper.DtoFromEntity(profile);
        } else
            throw new RessourceAlreadyDisabledException("Profile Already desactive");
    }

    public UserResponse changeActiveProfile(Long userId, Long profileId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with id:" + userId));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id:" + profileId));
        if (user.getActifProfile() != profile) {
            user.setActifProfile(profile);
            userRepository.save(user);
            return userMapper.DtoFromEntity(user);
        } else
            throw new ProfileAlreadyActiveException("Profile Already Active");
    }

    public ProfileResponse addRoleToProfile(Long profileId, List<Long> roleIds) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id:" + profileId));
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id: " + roleId));
            profile.addRole(role);
        }
        profileRepository.save(profile);
        return profileMapper.DtoFromEntity(profile);
    }

    public ProfileResponse removeRoleFromProfile(Long profileId, List<Long> roleIds) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id:" + profileId));
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new MyResourceNotFoundException("Role not found with id: " + roleId));
            profile.removeRole(role);
        }
        profileRepository.save(profile);
        return profileMapper.DtoFromEntity(profile);
    }

    public ProfileResponse addModuleToProfile(Long profileId, Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + moduleId));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id:" + profileId));
        profile.addModule(module);
        profileRepository.save(profile);
        return profileMapper.DtoFromEntity(profile);
    }

    public ProfileResponse removeModuleFromProfile(Long profileId, Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + moduleId));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id:" + profileId));
        profile.removeModule(module);
        profileRepository.save(profile);
        return profileMapper.DtoFromEntity(profile);
    }

    public ProfileResponse addProfileToGroupe(Long profileId, Long groupeId) {
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new MyResourceNotFoundException("Groupe not found with id: " + groupeId));
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id:" + profileId));
        profile.setGroupe(groupe);
        profileRepository.save(profile);
        return profileMapper.DtoFromEntity(profile);
    }

    public ProfileResponse removeProfileFromGroupe(Long profileId, Long groupeId) {
        Groupe groupe = null;
        if (groupeId != -1) {
            groupe = groupeRepository.findById(groupeId)
                    .orElseThrow(() -> new MyResourceNotFoundException("Groupe not found with id: " + groupeId));
        }
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id:" + profileId));
        profile.setGroupe(groupe);
        profileRepository.save(profile);
        return profileMapper.DtoFromEntity(profile);
    }

    public List<ProfileResponse> getProfileByUser(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with user name:" + userName));
        List<Profile> profiles = profileRepository.findByUser(user);
        return profiles.stream().map(profileMapper::DtoFromEntity).collect(Collectors.toList());
    }

    public ProfileResponse addAuthorityToProfile(Long profileId, List<Long> authorityIds) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id:" + profileId));

        for (Long authorityId : authorityIds) {
            Authority authority = authorityRepository.findById(authorityId)
                    .orElseThrow(() -> new MyResourceNotFoundException("Authority not found with id: " + authorityId));

            ProfileAuthority profileAuthority = ProfileAuthority.builder()
                    .authority(authority)
                    .profile(profile)
                    .granted(true)
                    .build();

            profile.addAuthority(profileAuthority);
            profileAuthorityRepository.save(profileAuthority);
        }
        profileRepository.save(profile);
        return profileMapper.DtoFromEntity(profile);
    }

    public void removeAuthorityFromProfile(Long profileId, List<Long> authorityIds) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id: " + profileId));

        for (Long authorityId : authorityIds) {
            ProfileAuthority profileAuthority = profileAuthorityRepository.findByProfileIdAndAuthorityId(profileId, authorityId)
                    .orElseThrow(() -> new MyResourceNotFoundException("ProfileAuthority not found for profileId: " + profileId + " and authorityId: " + authorityId));

            profile.removeAuthority(profileAuthority);
            profileAuthorityRepository.delete(profileAuthority);
        }
        profileRepository.save(profile);
    }

    //    public ProfileAuthorityResponse revokeAuthorityFromProfile(Long profileAuthorityId){
//        ProfileAuthority profileAuthority=profileAuthorityRepository.findById(profileAuthorityId)
//                .orElseThrow(()->new MyResourceNotFoundException("Profile Authority not found with id: "+profileAuthorityId));
//        profileAuthority.setGranted(false);
//        profileAuthorityRepository.save(profileAuthority);
//        return profileMapper.profileAuthorityToResponse(profileAuthority);
//    }
//
//    public ProfileAuthorityResponse granteAuthorityFromProfile(Long profileAuthorityId) {
//        ProfileAuthority profileAuthority=profileAuthorityRepository.findById(profileAuthorityId)
//                .orElseThrow(()->new MyResourceNotFoundException("Profile Authority not found with id: "+profileAuthorityId));
//        profileAuthority.setGranted(true);
//        profileAuthorityRepository.save(profileAuthority);
//        return profileMapper.profileAuthorityToResponse(profileAuthority);
//    }
    public Set<String> getUserUuidforGroupe(Long id) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("Groupe not found with id: " + id));
        List<Profile> profiles = profileRepository.findAllByGroupe(groupe);
        Set<User> users = profiles.stream()
                .map(Profile::getUser)
                .collect(Collectors.toSet());
        Set<String> uuids = users.stream()
                .map(User::getUuid)
                .collect(Collectors.toSet());
        return uuids;
    }
    public Long count(){
        return profileRepository.count();
    }
//    public Long countByModule(Long id){
//        Module module=moduleRepository.findById(id)
//                .orElseThrow(() -> new MyResourceNotFoundException("Module not found with id: " + id));
//        return profileRepository.countByModule(module);
//    }
}