package com.example.notificationservice.Config.Security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component("authz")
public class AuthorizationLogic {

    @Value("${permission-service.uri}")
    private String permissionServiceUri;

    @Value("${permission-service.authority-param-name}")
    private String authorityParamName;

    @Value("${permission-service.module-param-name}")
    private String moduleParamName;

    @Value("${permission-service.module-name}")
    private String moduleName;

    public boolean hasCustomAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            Jwt jwt = jwtAuthentication.getToken();

            String url = UriComponentsBuilder.fromUriString(permissionServiceUri)
                    .queryParam(authorityParamName, authority)
                    .queryParam(moduleParamName, moduleName)
                    .build()
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwt.getTokenValue());

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            try {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Boolean> response = restTemplate.exchange(url, HttpMethod.GET, entity, Boolean.class);
                return Boolean.TRUE.equals(response.getBody());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return false;
    }
}