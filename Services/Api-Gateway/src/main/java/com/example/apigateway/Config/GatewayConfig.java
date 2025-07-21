package com.example.apigateway.Config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route pour admin-service
                .route("admin-service", r -> r.path("/api/v1/admin/**")
                        .uri("lb://admin-service"))
                .route("auditlog-service", r -> r.path("/api/v1/audit-log/**")
                        .uri("lb://auditlog-service"))
                // Route pour Authentifcationservice
                .route("authentification-service", r -> r.path("/api/v1/auth-service/**")
                        .uri("lb://authentification-service"))
//                 Route pour discovery-service (Eureka)
                .route("discovery-service", r -> r.path("/eureka/**")
                        .uri("http://localhost:8761"))

                .build();
    }
}
