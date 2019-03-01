package com.lhm.webflux.controller;

import com.lhm.webflux.entity.User;
import com.lhm.webflux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/user")
public class FluxController {

    @Autowired
    private UserService userService;

    @RequestMapping("test1")
    public Mono<String> test1() {
        return Mono.just("草泥马的比");
    }

    @RequestMapping("save")
    public Mono<User> save(User user) {
        return userService.save(user);
    }

    @RequestMapping("{username}")
    public Mono<User> find(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<User> findAll() {
        return this.userService.findAll().delayElements(Duration.ofSeconds(1));
    }

}
