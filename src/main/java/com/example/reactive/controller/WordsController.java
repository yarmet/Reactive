package com.example.reactive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WordsController {

    @GetMapping("/get")
    public Mono<String> getData() {
        return Mono.just("some text");
    }

}
