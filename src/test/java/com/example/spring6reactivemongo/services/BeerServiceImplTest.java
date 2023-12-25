package com.example.spring6reactivemongo.services;

import com.example.spring6reactivemongo.domain.Beer;
import com.example.spring6reactivemongo.mappers.BeerMapper;
import com.example.spring6reactivemongo.model.BeerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerServiceImplTest {
    @Autowired
    BeerService beerService;
    @Autowired
    BeerMapper beerMapper;

    BeerDto beerDto;

    @BeforeEach
    void setUp() {
        beerDto = beerMapper.beerToBeerDto(getTestBeer());
    }

    @Test
    void saveBeer() throws InterruptedException {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Mono<BeerDto> savedMono = beerService.saveBeer(Mono.just(beerDto));
        savedMono.subscribe(savedDto -> {
            System.out.println(savedDto.getId());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    public static Beer getTestBeer() {
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(12)
                .upc("123213")
                .build();
    }
}