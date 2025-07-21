package com.example.adminservice.Services;

import com.example.adminservice.Config.Exceptions.UserAlreadyExistsException;
import com.example.adminservice.Config.Exceptions.UserCreationException;
import com.example.adminservice.Config.Security.KeycloakProvider;
import com.example.adminservice.Dto.User.UserRequest;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@Transactional
@Service
public class KeycloakService {
    //    public List<UserRepresentation> findAllUsers(){
//        return KeycloakProvider.getRealmResource()
//                .users()
//                .list();
//    }
//    public List<UserRepresentation> searchUserByUsername(String username) {
//        return KeycloakProvider.getRealmResource()
//                .users()
//                .searchByAttributes(username);
//    }
    public String createUser(UserRequest userRequest) {
        int status = 0;
        UsersResource usersResource = KeycloakProvider.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userRequest.getFirstName());
        userRepresentation.setLastName(userRequest.getLastName());
        userRepresentation.setEmail(userRequest.getEmail());
        userRepresentation.setUsername(userRequest.getUserName());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        Response response = usersResource.create(userRepresentation);

        status = response.getStatus();

        if (status == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue("admin123");

            usersResource.get(userId).resetPassword(credentialRepresentation);


            return org.keycloak.admin.client.CreatedResponseUtil.getCreatedId(response);

        } else if (status == 409) {
            throw new UserAlreadyExistsException("User already exists!");
        } else {
            throw new UserCreationException("Error creating user, please contact with the administrator.");
        }
    }

    public void deleteUser(String uuid) {
        KeycloakProvider.getUserResource()
                .get(uuid)
                .remove();
    }

    public void updateUser(String uuid, UserRequest userRequest) {

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue("admin123");

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRequest.getUserName());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource usersResource = KeycloakProvider.getUserResource().get(uuid);
        usersResource.update(user);
    }

    public void enableDisableUser(String uuid, Boolean enable) {
        UserResource userResource = KeycloakProvider.getUserResource().get(uuid);
        UserRepresentation user = userResource.toRepresentation();
        user.setEnabled(enable);
        userResource.update(user);
    }
}
