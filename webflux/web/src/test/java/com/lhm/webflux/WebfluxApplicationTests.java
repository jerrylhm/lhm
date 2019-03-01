package com.lhm.webflux;

import com.lhm.webflux.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebfluxApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void webClient() throws InterruptedException{
        WebClient webClient = WebClient.create("http://127.0.0.1:8080"); // 1
        webClient
                .get().uri("/user")
                .exchange() // 3
                .flatMapMany(response -> response.bodyToFlux(User.class))   // 4
                .log()
                .blockLast();   // 6
    }

    @Test
    public void webClientTest3() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient
                .get().uri("/times")
                .accept(MediaType.TEXT_EVENT_STREAM)    // 1
                .retrieve()
                .bodyToFlux(String.class)
                .log()  // 2
                .take(10)   // 3
                .blockLast();
    }

    @Test
    public void webClientTest4() {
        Flux<String> eventFlux = Flux.interval(Duration.ofSeconds(1))
                .map(l -> {
                    System.out.println("正在生成草泥马" + l + "号");
                    return "fuck hahaha";}).take(10); // 1
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient
                .post().uri("/events")
                .contentType(MediaType.APPLICATION_STREAM_JSON) // 2
                .body(eventFlux, String.class) // 3
                .retrieve()
                .bodyToFlux(String.class)
                .log()
                .blockLast();
    }
}
