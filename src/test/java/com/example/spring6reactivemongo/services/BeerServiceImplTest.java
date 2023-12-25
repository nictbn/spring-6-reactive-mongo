package com.example.spring6reactivemongo.services;

import com.example.spring6reactivemongo.domain.Beer;
import com.example.spring6reactivemongo.mappers.BeerMapper;
import com.example.spring6reactivemongo.mappers.BeerMapperImpl;
import com.example.spring6reactivemongo.model.BeerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

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
    @DisplayName("Test Save Beer Using Subscriber")
    void saveBeerUseSubscriber() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDto> atomicDto = new AtomicReference<>();
        Mono<BeerDto> savedMono = beerService.saveBeer(Mono.just(beerDto));
        savedMono.subscribe(savedDto -> {
            System.out.println(savedDto.getId());
            atomicBoolean.set(true);
            atomicDto.set(savedDto);
        });
        await().untilTrue(atomicBoolean);
        BeerDto persistedDto = atomicDto.get();
        assertThat(persistedDto).isNotNull();
        assertThat(persistedDto.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Save Beer Using Block")
    void testSaveBeerUseBlock() {
        BeerDto savedDto = beerService.saveBeer(Mono.just(getTestBeerDto())).block();
        assertThat(savedDto).isNotNull();
        assertThat(savedDto.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Update Beer Using Block")
    void testUpdateBlocking() {
        final String newName = "New Beer Name";
        BeerDto savedBeerDto = getSavedBeerDto();
        savedBeerDto.setBeerName(newName);
        BeerDto updatedDto = beerService.saveBeer(Mono.just(savedBeerDto)).block();
        BeerDto fetchedDto = beerService.getById(updatedDto.getId()).block();
        assertThat(fetchedDto.getBeerName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("Test Update Using Reactive Streams")
    void testUpdateStreaming() {
        final String newName = "New Beer Name";  // use final so cannot mutate

        AtomicReference<BeerDto> atomicDto = new AtomicReference<>();

        beerService.saveBeer(Mono.just(getTestBeerDto()))
                .map(savedBeerDto -> {
                    savedBeerDto.setBeerName(newName);
                    return savedBeerDto;
                })
                .flatMap(beerService::saveBeer)
                .flatMap(savedUpdatedDto -> beerService.getById(savedUpdatedDto.getId()))
                .subscribe(atomicDto::set);

        await().until(() -> atomicDto.get() != null);
        assertThat(atomicDto.get().getBeerName()).isEqualTo(newName);
    }

    @Test
    void testDeleteBeer() {
        BeerDto beerToDelete = getSavedBeerDto();
        beerService.deleteBeerById(beerToDelete.getId()).block();
        Mono<BeerDto> expectedEmptyBeerMono = beerService.getById(beerToDelete.getId());
        BeerDto emptyBeer = expectedEmptyBeerMono.block();
        assertThat(emptyBeer).isNull();

    }

    @Test
    void findFirstByBeerNameTest() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        BeerDto beerDto = getSavedBeerDto();
        Mono<BeerDto> foundDto = beerService.findFirstByBeerName(beerDto.getBeerName());
        foundDto.subscribe(dto -> {
            System.out.println(dto);
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testFindByBeerStyle() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        BeerDto beerDto = getSavedBeerDto();
        beerService.findByBeerStyle(beerDto.getBeerStyle())
                .subscribe(dto -> {
                    System.out.println(dto);
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);

    }

    public BeerDto getSavedBeerDto(){
        return beerService.saveBeer(Mono.just(getTestBeerDto())).block();
    }

    public static BeerDto getTestBeerDto(){
        return new BeerMapperImpl().beerToBeerDto(getTestBeer());
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