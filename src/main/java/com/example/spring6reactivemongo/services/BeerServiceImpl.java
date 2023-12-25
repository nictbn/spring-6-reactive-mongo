package com.example.spring6reactivemongo.services;

import com.example.spring6reactivemongo.model.BeerDto;
import reactor.core.publisher.Mono;

public class BeerServiceImpl implements BeerService {
    @Override
    public Mono<BeerDto> getById(BeerDto beerDto) {
        return null;
    }

    @Override
    public Mono<BeerDto> saveBeer(BeerDto beerDto) {
        return null;
    }
}
