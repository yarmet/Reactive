package com.example.reactive.service;

import com.example.reactive.repository.User;
import com.example.reactive.repository.UserRepo;
import com.example.reactive.repository.UserRole;
import com.example.reactive.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
@Component
@AllArgsConstructor
public class UserService {

    private final static ResponseEntity<HttpStatus> UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


    public Mono<ResponseEntity<?>> getToken(ServerWebExchange swe) {
        return swe.getFormData().flatMap(credentials -> {

                    String username = credentials.getFirst("username");
                    String password = credentials.getFirst("password");

                    return userRepo.findByUsername(username)
                            .map(userFromDb -> passwordEncoder.matches(password, userFromDb.getPassword())
                                    ? ResponseEntity.ok(jwtUtil.generateToken(userFromDb))
                                    : UNAUTHORIZED
                            )
                            .defaultIfEmpty(UNAUTHORIZED);
                }
        );
    }


    public Mono<ResponseEntity<?>> save(ServerWebExchange swe) {

        return swe.getFormData().flatMap(credentials -> {

            String username = credentials.getFirst("username");
            String password = credentials.getFirst("password");

            User user = this.makeUser(username, password, UserRole.ROLE_ADMIN);

            return userRepo.save(user)
                    .map(ResponseEntity::ok)
                    .onErrorReturn(ResponseEntity.badRequest().build());
        });
    }


    private User makeUser(String username, String password, UserRole userRole) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(userRole);
        return user;
    }


}
