package com.example.spring6reactivemongo.webfn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class BeerRouter {
    public static final String BEER_PATH = "/api/v3/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    private final BeerHandler handler;
    @Bean
    public RouterFunction<ServerResponse> beerRoutes() {
        return route()
                .GET(BEER_PATH, accept(MediaType.APPLICATION_JSON), handler::listBeers)
                .GET(BEER_PATH_ID, accept(MediaType.APPLICATION_JSON), handler::getBeerById)
                .POST(BEER_PATH, accept(MediaType.APPLICATION_JSON), handler::createNewBeer)
                .PUT(BEER_PATH_ID, accept(MediaType.APPLICATION_JSON), handler::updateBeerById)
                .PATCH(BEER_PATH_ID, accept(MediaType.APPLICATION_JSON), handler::patchBeerById)
                .DELETE(BEER_PATH_ID, accept(MediaType.APPLICATION_JSON), handler::deleteBeerById)
                .build();
    }
}
