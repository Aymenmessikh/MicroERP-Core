//package com.example.adminservice.Config.Security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
//public class SecurityConfiguration {
//
//    private final CustomJwtAuthenticationConverte customJwtAuthenticationConverte;
//
//    @Autowired
//    public SecurityConfiguration(CustomJwtAuthenticationConverte customJwtAuthenticationConverte) {
//        this.customJwtAuthenticationConverte = customJwtAuthenticationConverte;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(
//                                "/v2/api-docs",
//                                "/v3/api-docs",
//                                "/v3/api-docs/**",
//                                "/swagger-resources",
//                                "/swagger-resources/**",
//                                "/configuration/ui",
//                                "/configuration/security",
//                                "/swagger-ui/**",
//                                "/webjars/**",
//                                "/swagger-ui.html"
//                        ).permitAll() // Allow public access to Swagger URLs
//                        .anyRequest().authenticated() // Require auth for everything else
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt
//                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                        )
//                );
//        return http.build();
//    }
//
//    @Bean
//    public JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//        converter.setJwtGrantedAuthoritiesConverter(jwt -> customJwtAuthenticationConverte
//                .extractAuthorities(jwt));
//        return converter;
//    }
//}
