package com.ibsu.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GatewayFilterFactory<JwtAuthFilter.Config> {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            System.out.println("Gateway received request for path: " + request.getURI().getPath());
            System.out.println("Auth header: " + request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (request.getURI().getPath().startsWith("/auth") &&
                    !request.getURI().getPath().equals("/auth/user") &&
                    !request.getURI().getPath().equals("/auth/edit") &&
                    !request.getURI().getPath().startsWith("/auth/admin")) {
                return chain.filter(exchange);
            }

            if (exchange.getRequest().getPath().toString().equals("/item/get-all")) {
                return chain.filter(exchange); // Skip JWT check
            }
            if (request.getURI().getPath().startsWith("/item/get")) {
                return chain.filter(exchange); // Skip JWT check
            }
            if (request.getURI().getPath().startsWith("/cart/exists") && (authHeader == null || !authHeader.startsWith("Bearer "))) {
                return chain.filter(exchange); // Skip JWT check
            }

            if (request.getURI().getPath().startsWith("/ws-items")) {
                return chain.filter(exchange); // Skip JWT check
            }

            if (request.getURI().getPath().startsWith("/topic")) {
                return chain.filter(exchange); // Skip JWT check
            }



            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorized(exchange);
            }

            try {
                String token = authHeader.substring(7);
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("Authorization", authHeader)
                        .header("X-Username", claims.getSubject())
                        .header("X-User-Id", claims.get("userId").toString())
                        .header("X-Role", claims.get("role").toString())
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (JwtException e) {
                return unauthorized(exchange);
            }
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
    }
}

