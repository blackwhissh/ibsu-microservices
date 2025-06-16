package com.ibsu.api_gateway.config;

import com.ibsu.api_gateway.filter.JwtAuthFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class FilterConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder, JwtAuthFilter jwtAuthFilter) {
        return builder.routes()
                .route("auth-user-filtered", r -> r.path("/auth/user")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://auth-service"))
                .route("auth-user-edit-filtered", r -> r.path("/auth/edit")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://auth-service"))
                .route("auth-service-open", r -> r.path("/auth/**")
                        .and().not(ra -> ra.path("/auth/user")) // Exclude /auth/user
                        .uri("lb://auth-service"))
                .route("order-service", r -> r.path("/order/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://order-service"))
                .route("profile-service", r -> r.path("/profile/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://profile-service"))
                .route("item-service", r -> r.path("/item/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://item-service"))
                .route("item-ws", r -> r
                        .path("/ws-items/**")
                        .uri("lb://item-service"))
                .route("cart-service", r -> r.path("/cart/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
                        .uri("lb://cart-service"))

                .build();
    }
}

