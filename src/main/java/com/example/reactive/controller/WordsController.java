package com.example.reactive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@RestController
public class WordsController {

    @GetMapping("/getGroups")
    public Mono<List<String>> getData(ServerWebExchange swe) {
        return swe.getPrincipal().map(Principal::getName)
                .flatMap(this::loadGroupsByName);
    }

    // todo тестовая заглушка, пернести в сервис и реализовать полноценно.
    private Mono<List<String>> loadGroupsByName(String name) {
        List<String> groups = List.of("group1", "group2", "group3");
        return Mono.just(groups);
    }

}
