package com.example.reactive.security;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        try {
            String username = jwtUtil.extractUsername(authToken);

            if (username == null || !jwtUtil.validateToken(authToken)) {
                return Mono.empty();
            }
            Claims claims = jwtUtil.getClaimsFromToken(authToken);
            List<String> role = claims.get("role", List.class);

            List<SimpleGrantedAuthority> authorities = role.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return Mono.just(new UsernamePasswordAuthenticationToken(username, null, authorities));
        } catch (Exception e) {
            log.error(null, e);
            return Mono.empty();
        }
    }

}


