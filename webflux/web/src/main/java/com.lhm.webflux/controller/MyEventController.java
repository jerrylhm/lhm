package com.lhm.webflux.controller;

import com.lhm.webflux.entity.MyEvent;
import com.lhm.webflux.repository.MyEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
    @RequestMapping("/events")
    public class MyEventController {
        @Autowired
        private MyEventRepository myEventRepository;

        @PostMapping(path = "", consumes = MediaType.APPLICATION_STREAM_JSON_VALUE) // 1
        public Flux<String> loadEvents(@RequestBody Flux<String> events) {

            return Flux.just("我是你爹");    // 2
        }

        @GetMapping(path = "", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
        public Flux<MyEvent> getEvents() {
            return this.myEventRepository.findBy();
        }

    }