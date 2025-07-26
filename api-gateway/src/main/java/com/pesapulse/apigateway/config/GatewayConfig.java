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
                // If a request comes in with a path like /api/users/register...
                .route("user-service", r -> r.path("/api/users/**")
                        // ...forward it to the user-service running on port 8081.
                        .uri("http://localhost:8081"))

                // Route for the Transaction Service
                // If a request comes in with a path like /api/transactions/upload...
                .route("transaction-service", r -> r.path("/api/transactions/**")
                        // ...forward it to the transaction-service running on port 8082.
                        .uri("http://localhost:8082"))
                
                .build();
    }
}