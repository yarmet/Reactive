package com.example.reactive.controller;

import com.example.reactive.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@AllArgsConstructor
public class UserController {


    private final UserService userService;


    @PostMapping("/test")
    public Mono<?> test(ServerWebExchange swe) {
        return swe.getPrincipal().map(p -> "Hello " + p.getName());
    }


    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(ServerWebExchange swe) {
        return userService.getToken(swe);
    }


    @PostMapping("/registry")
    public Mono<ResponseEntity<?>> registry(ServerWebExchange swe) {
        return userService.save(swe);
    }

}
