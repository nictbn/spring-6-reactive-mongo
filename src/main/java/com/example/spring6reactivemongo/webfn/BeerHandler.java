package com.example.spring6reactivemongo.webfn;

import com.example.spring6reactivemongo.model.BeerDto;
import com.example.spring6reactivemongo.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static com.example.spring6reactivemongo.webfn.BeerRouter.BEER_PATH_ID;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;

    public Mono<ServerResponse> listBeers(ServerRequest request) {
        return ServerResponse.ok().body(beerService.listBeers(), BeerDto.class);
    }

    public Mono<ServerResponse> getBeerById(ServerRequest request) {
        return ServerResponse.ok().body(beerService.getById(request.pathVariable("beerId")), BeerDto.class);
    }

    public Mono<ServerResponse> createNewBeer(ServerRequest request) {
        return beerService.saveBeer(request.bodyToMono(BeerDto.class))
                .flatMap(beerDto -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(BEER_PATH_ID)
                                .build(beerDto.getId())).build());
    }
}
