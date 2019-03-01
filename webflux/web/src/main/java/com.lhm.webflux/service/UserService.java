package com.lhm.webflux.service;

import com.lhm.webflux.entity.User;
import com.lhm.webflux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public static void main(String[] args) {
        String[][] strs = new String[][]{{"a","b"},{"c","d"}};
        Arrays.stream(strs).flatMap(e -> Arrays.stream(e)).forEach(System.out::println);
    }
}
