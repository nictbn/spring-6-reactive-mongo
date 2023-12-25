package com.example.spring6reactivemongo.services;

import com.example.spring6reactivemongo.model.BeerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {
    Flux<BeerDto> listBeers();
    Mono<BeerDto> saveBeer(Mono<BeerDto> beerDto);
    Mono<BeerDto> saveBeer(BeerDto beerDto);
    Mono<BeerDto> getById(String beerId);
    Mono<BeerDto> updateBeer(String beerId, BeerDto beerDto);
    Mono<BeerDto> patchBeer(String beerId, BeerDto beerDto);
    Mono<Void> deleteBeerById(String beerId);
}
