package com.pesapulse.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining API Gateway routes programmatically.
 */
 @Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route for the User Service
                // Requests to /api/users/** are now forwarded to the 'user-service' container.
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("http://user-service:8081"))

                // Route for the Transaction Service
                // Requests to /api/transactions/** are now forwarded to the 'transaction-service' container.
                .route("transaction-service", r -> r.path("/api/transactions/**")
                        .uri("http://transaction-service:8082"))
                
                .build();
    }
}