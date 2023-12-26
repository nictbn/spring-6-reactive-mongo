package com.example.spring6reactivemongo.services;

import com.example.spring6reactivemongo.model.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerDto> listCustomers();

    Mono<CustomerDto> getCustomerById(String customerId);

    Mono<CustomerDto> saveNewCustomer(CustomerDto customerDTO);

    Mono<CustomerDto> saveNewCustomer(Mono<CustomerDto> customerDTO);

    Mono<CustomerDto> updateCustomer(String customerId, CustomerDto customerDTO);

    Mono<CustomerDto> patchCustomer(String customerId, CustomerDto customerDTO);

    Mono<Void> deleteCustomerById(String customerId);
}
