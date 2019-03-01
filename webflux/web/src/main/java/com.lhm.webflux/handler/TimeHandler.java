package com.lhm.webflux.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

@Component
public class TimeHandler {

    public Mono<ServerResponse> getTime(ServerRequest request) {

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header("Content-Type", "text/plain; charset=utf-8")
                .body(Mono.just("当前时间:" + new SimpleDateFormat("HH:mm:ss").format(new Date())), String.class);
    }

    public Mono<ServerResponse> getDate(ServerRequest request) {
        //设置头信息，防止中文乱码
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put("Content-Type", Arrays.asList("text/plain; charset=utf-8"));

        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
                .headers(httpHeaders -> httpHeaders.addAll(header))
                .body(Mono.just("当前日期:" + new SimpleDateFormat("yyyy-MM-dd").format(new Date())), String.class);
    }

    public Mono<ServerResponse> getTimeSec() {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(Flux.interval(Duration.ofSeconds(1)).map(l ->
                {
                    System.out.println(l);
                    return new SimpleDateFormat("HH:mm:ss").format(new Date());
                }
                )
                , String.class);

    }
}
