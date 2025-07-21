package com.example.adminservice.Services;

import com.example.adminservice.Config.Exceptions.MyResourceNotFoundException;
import com.example.adminservice.Config.filter.clause.Clause;
import com.example.adminservice.Config.filter.specification.GenericSpecification;
import com.example.adminservice.Dto.Profile.ProfileResponse;
import com.example.adminservice.Dto.User.BlockchainRegisterRequest;
import com.example.adminservice.Dto.User.UserProfileInfoDto;
import com.example.adminservice.Dto.User.UserRequest;
import com.example.adminservice.Dto.User.UserResponse;
import com.example.adminservice.Entity.Profile;
import com.example.adminservice.Entity.ProfileAuthority;
import com.example.adminservice.Entity.User;
import com.example.adminservice.Mapper.Profile.ProfileMapper;
import com.example.adminservice.Mapper.User.UserMapper;
import com.example.adminservice.Repository.ProfileRepository;
import com.example.adminservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final KeycloakService keycloakService;
    private final UserMapper userMapper;
    private final ProfileMapper profileMapper;
    //    private final KafkaEvents kafkaEvents;
    private final RestTemplate restTemplate;


    public UserResponse createUser(UserRequest userRequest, Long profileTypeId) {
        String uuid = keycloakService.createUser(userRequest);
        if (uuid == null) {
            throw new IllegalStateException("UUID is null. User creation in Keycloak failed.");
        }

        User user = userMapper.EntityFromDto(userRequest);
        user.setUuid(uuid);
        userRepository.save(user);
        Profile defaultProfile = (profileTypeId == -1) ? createDefaultProfile(user) : cloneExistingProfile(user, profileTypeId);
        user.setActifProfile(defaultProfile);
        user.addProfile(defaultProfile);
        userRepository.save(user);
        registerOnBlockchain(userRequest.getAddress(), userRequest.getUserName());
//         triggerUserEvent(createdUser);
        return userMapper.DtoFromEntity(user);
    }

    public void registerOnBlockchain(String address, String username) {
        String blockchainUrl = "http://localhost:3000/users/register"; // Update with your Express.js host

        BlockchainRegisterRequest request = new BlockchainRegisterRequest(address, username);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(blockchainUrl, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("User registered on blockchain successfully");
            } else {
                System.out.println("Blockchain registration failed: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error calling blockchain service: " + e.getMessage());
        }
    }


    // Crée un profil par défaut
    private Profile createDefaultProfile(User user) {
        Profile profile = Profile.builder()
                .libelle("Default Profile")
                .actif(true)
                .user(user)
                .build();
        return profileRepository.save(profile);
    }

    // Clone un profil existant
    private Profile cloneExistingProfile(User user, Long profileTypeId) {
        Profile profile = profileRepository.findById(profileTypeId)
                .orElseThrow(() -> new MyResourceNotFoundException("Profile not found with id: " + profileTypeId));

        Profile clonedProfile = Profile.builder()
                .libelle(profile.getLibelle())
                .groupe(profile.getGroupe())
                .roles(new ArrayList<>(profile.getRoles()))
                .modules(new ArrayList<>(profile.getModules()))
                .actif(true)
                .user(user)
                .build();

        Profile savedProfile = profileRepository.save(clonedProfile);

        // Cloner les autorités associées
        List<ProfileAuthority> newAuthorities = profile.getProfileAuthorities().stream()
                .map(auth -> ProfileAuthority.builder()
                        .authority(auth.getAuthority())
                        .granted(auth.getGranted())
                        .profile(savedProfile)
                        .build())
                .collect(Collectors.toList());

        savedProfile.setProfileAuthorities(newAuthorities);
        return profileRepository.save(savedProfile);
    }

// déclencher un événement Kafka
// private void triggerUserEvent(User user) {
//     UserEvent userEvent = UserEvent.UserEventFromEntity(user);
//     kafkaEvents.sendUserToNotificationService(userEvent);
// }

    public PageImpl<UserResponse> getAllUsers(List<Clause> filter, PageRequest pageRequest) {
        Specification<User> specification = new GenericSpecification<>(filter);

        Page<User> page = userRepository.findAll(specification, pageRequest);

        List<UserResponse> userResponses = page.getContent().stream()
                .map(userMapper::DtoFromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(userResponses, pageRequest, page.getTotalElements());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with id:" + id));
        return userMapper.DtoFromEntity(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with Email:" + email));
        return userMapper.DtoFromEntity(user);
    }

    public User getUserByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with userName:" + userName));
        return user;
    }

    public UserResponse updateUser(UserRequest userRequest, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with id:" + id));
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        userRepository.save(user);
//        keycloakService.updateUser(user.getUuid(),userRequest);
        return userMapper.DtoFromEntity(user);
    }

    public UserResponse enableDisabeleUser(Long id, Boolean enable) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with id:" + id));
//         keycloakService.enableDisableUser(user.getUuid(), enable);
        user.setActif(enable);
        userRepository.save(user);
        return userMapper.DtoFromEntity(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new MyResourceNotFoundException("User not found with id:" + id));
        keycloakService.deleteUser(user.getUuid());
        userRepository.delete(user);
    }

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::DtoFromEntity).collect(Collectors.toList());
    }

    public Long count() {
        return userRepository.count();
    }

    public Long countActifUser() {
        return userRepository.countByActifTrue();
    }
    public List<UserProfileInfoDto> getUsersByModuleId(Long moduleId) {
        return userRepository.findUsersByModuleId(moduleId);
    }

    public UserResponse changeActiveProfile(Long id, String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow();
        Profile newActiveProfile = profileRepository.findById(id)
                .orElseThrow();
        user.setActifProfile(newActiveProfile);
        user = userRepository.save(user);
        return userMapper.DtoFromEntity(user);
    }
    public ProfileResponse getActifeProfile(String username){
        User user=userRepository.findByUserName(username).orElseThrow();
        Profile profile=user.getActifProfile();
        return profileMapper.DtoFromEntity(profile);
    }
    public List<ProfileResponse> getInActiveProfile(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new MyResourceNotFoundException("User not found"));

        Profile activeProfile = user.getActifProfile();
        List<Profile> inactiveProfiles = user.getProfiles().stream()
                .filter(profile -> !profile.equals(activeProfile))
                .collect(Collectors.toList());

        return inactiveProfiles.stream()
                .map(profileMapper::DtoFromEntity)
                .collect(Collectors.toList());
    }
}
