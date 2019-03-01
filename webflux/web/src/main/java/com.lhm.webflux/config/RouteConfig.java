package com.lhm.webflux.config;

import com.lhm.webflux.handler.TimeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouteConfig {
    @Autowired
    private TimeHandler timeHandler;

    @Bean
    public RouterFunction<ServerResponse> timeRouter() {
        return RouterFunctions.route(RequestPredicates.GET("/time"), request -> timeHandler.getTime(request))
                .andRoute(RequestPredicates.GET("/date"), request -> timeHandler.getDate(request))
                .andRoute(RequestPredicates.GET("/times"), request -> timeHandler.getTimeSec());
    }
}
