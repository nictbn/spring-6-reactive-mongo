package com.example.spring6reactivemongo.services;

import com.example.spring6reactivemongo.mappers.BeerMapper;
import com.example.spring6reactivemongo.model.BeerDto;
import com.example.spring6reactivemongo.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {
    private final BeerMapper beerMapper;
    private final BeerRepository beerRepository;
    @Override
    public Mono<BeerDto> getById(BeerDto beerDto) {
        return null;
    }

    @Override
    public Mono<BeerDto> saveBeer(Mono<BeerDto> beerDto) {
        return beerDto.map(beerMapper::beerDtoToBeer)
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDto);
    }
}
