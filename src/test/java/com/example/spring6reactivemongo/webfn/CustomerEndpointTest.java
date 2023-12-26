package com.example.spring6reactivemongo.webfn;

import com.example.spring6reactivemongo.model.CustomerDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.example.spring6reactivemongo.webfn.CustomerRouter.CUSTOMER_PATH;
import static com.example.spring6reactivemongo.webfn.CustomerRouter.CUSTOMER_PATH_ID;
import static org.hamcrest.Matchers.greaterThan;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerEndpointTest {
    @Autowired
    WebTestClient webTestClient;

    @Test
    void testPatchIdNotFound() {
        webTestClient.patch()
                .uri(CUSTOMER_PATH_ID, 999)
                .body(Mono.just(getCustomerDto()), CustomerDto.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteNotFound() {
        webTestClient.delete()
                .uri(CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(4)
    void testUpdateCustomerBadRequest() {
        CustomerDto customerDto = getCustomerDto();
        customerDto.setCustomerName("");

        webTestClient.put()
                .uri(CUSTOMER_PATH_ID, 1)
                .body(Mono.just(customerDto), CustomerDto.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateCustomerNotFound() {
        webTestClient.put()
                .uri(CUSTOMER_PATH_ID, 999)
                .body(Mono.just(getCustomerDto()), CustomerDto.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetByIdNotFound() {
        webTestClient.get().uri(CUSTOMER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(999)
    void testDeleteCustomer() {
        CustomerDto dto = getSavedTestCustomer();

        webTestClient.delete()
                .uri(CUSTOMER_PATH_ID, dto.getId())
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @Order(3)
    void testUpdateCustomer() {
        CustomerDto dto = getSavedTestCustomer();
        webTestClient.put()
                .uri(CUSTOMER_PATH_ID, dto.getId())
                .body(Mono.just(dto), CustomerDto.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testCreateCustomer() {

        webTestClient.post().uri(CUSTOMER_PATH)
                .body(Mono.just(getCustomerDto()), CustomerDto.class)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location");
    }

    @Test
    @Order(1)
    void testGetById() {
        CustomerDto dto = getSavedTestCustomer();

        webTestClient.get().uri(CUSTOMER_PATH_ID, dto.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(CustomerDto.class);
    }

    @Test
    @Order(2)
    void testListCustomers() {
        webTestClient.get().uri(CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").value(greaterThan(1));
    }

    public CustomerDto getSavedTestCustomer(){
        FluxExchangeResult<CustomerDto> beerDTOFluxExchangeResult = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .body(Mono.just(getCustomerDto()), CustomerDto.class)
                .header("Content-Type", "application/json")
                .exchange()
                .returnResult(CustomerDto.class);

        List<String> location = beerDTOFluxExchangeResult.getResponseHeaders().get("Location");

        return webTestClient.get().uri(location.get(0))
                .exchange().returnResult(CustomerDto.class).getResponseBody().blockFirst();
    }

    public static CustomerDto getCustomerDto() {
        return CustomerDto.builder()
                .customerName("Test Customer")
                .build();
    }
}