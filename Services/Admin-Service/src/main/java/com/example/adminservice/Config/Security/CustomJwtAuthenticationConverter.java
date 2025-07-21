package com.example.adminservice.Config.Security;

import com.example.adminservice.Entity.Profile;
import com.example.adminservice.Entity.User;
import com.example.adminservice.Services.ExtracteAuthorityService;
import com.example.adminservice.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final UserService userService;
    private final ExtracteAuthorityService extracteAuthorityService;
    private static final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principle-attribute:sub}")
    private String principleAttribute;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // Extract username from JWT (this could be username or address based on your token structure)
        String username = jwt.getClaim(principleAttribute);
        // Extract base authorities from JWT
        Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
        Set<GrantedAuthority> finalAuthorities = new HashSet<>();
        if (authorities != null) {
            finalAuthorities.addAll(authorities);
        }
        try {
            // Get user from database
            User user = userService.getUserByUserName(username);
            // Check if user is active
            if (!user.getActif()) {
                throw new RuntimeException("This user is disabled");
            }
            // Add authorities from user's active profile
            Profile activeProfile = user.getActifProfile();
            if (activeProfile != null) {
                Set<GrantedAuthority> profileAuthorities = extracteAuthorityService.extractAuthority(activeProfile.getId());
                finalAuthorities.addAll(profileAuthorities);
            }
            // Create authentication token with username, address and authorities
            return new UsernamePasswordAuthenticationToken(
                    username,
                    null, // credentials not needed here
                    finalAuthorities
            );
        } catch (Exception e) {
            // Log error and return token with base authorities only
            System.err.println("Error loading user authorities: " + e.getMessage());
            return new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    finalAuthorities
            );
        }
    }
}
