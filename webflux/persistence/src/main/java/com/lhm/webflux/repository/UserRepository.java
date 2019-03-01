package com.lhm.webflux.repository;

import com.lhm.webflux.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {  // 1
        Mono<User> findByUsername(String username);     // 2
        Mono<Long> deleteByUsername(String username);
}