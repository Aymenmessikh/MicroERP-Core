package com.example.notificationservice.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class GroupeService {
    private final WebClient.Builder webClient;

    @Value("${groupe-service.uri}")
    private String groupeServiceUri;

    public Set<String> getUserUuidByGroupe(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            Jwt jwt = jwtAuthentication.getToken();
            String[] responseArray = webClient.build().get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/admin/profile/userUuid") // Use relative path
                            .queryParam("id", id)
                            .build())
                    .header("Authorization", "Bearer " + jwt.getTokenValue())
                    .retrieve()
                    .bodyToMono(String[].class)
                    .block();

            return new HashSet<>(Arrays.asList(responseArray));
        }
        else throw new RuntimeException();
    }
}
