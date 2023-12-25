package com.example.spring6reactivemongo.services;

import com.example.spring6reactivemongo.model.BeerDto;
import reactor.core.publisher.Mono;

public interface BeerService {

    Mono<BeerDto> getById(BeerDto beerDto);
    Mono<BeerDto> saveBeer(BeerDto beerDto);
}
