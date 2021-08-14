package com.example.reactive.controller;

import com.example.reactive.security.JwtUtil;
import com.example.reactive.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {

    private final static ResponseEntity<Object> UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public Mono<?> login(ServerWebExchange swe) {
        return swe.getFormData().flatMap(credentials -> {
                    String username = credentials.getFirst("username");
                    String password = credentials.getFirst("password");

                    return userService.findByUserName(username)
                            .map(userFromDb -> passwordEncoder.matches(password, userFromDb.getPassword())
                                    ? ResponseEntity.ok(jwtUtil.generateToken(userFromDb))
                                    : UNAUTHORIZED
                            )
                            .defaultIfEmpty(UNAUTHORIZED);
                }
        );
    }


    @PostMapping("/registry")
    public Mono<?> registry(ServerWebExchange swe) {
        return swe.getFormData().flatMap(credentials -> {
            String username = credentials.getFirst("username");
            String password = credentials.getFirst("password");
            return userService.save(username, password);
        });
    }


    @GetMapping("/get")
    public Mono<String> getData() {
        return Mono.just("werwer");
    }


}
