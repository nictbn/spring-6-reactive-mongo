package com.example.spring6reactivemongo.services;

import com.example.spring6reactivemongo.mappers.CustomerMapper;
import com.example.spring6reactivemongo.model.CustomerDto;
import com.example.spring6reactivemongo.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Flux<CustomerDto> listCustomers() {
        return customerRepository.findAll()
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> getCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> saveNewCustomer(CustomerDto customerDto) {
        return customerRepository.save(customerMapper.customerDtoToCustomer(customerDto))
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> saveNewCustomer(Mono<CustomerDto> customerDto) {
        return customerDto.map(customerMapper::customerDtoToCustomer)
                .flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> updateCustomer(String customerId, CustomerDto customerDto) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    customer.setCustomerName(customerDto.getCustomerName());
                    return customer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> patchCustomer(String customerId, CustomerDto customerDto) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    if (StringUtils.hasText(customerDto.getCustomerName())){
                        customer.setCustomerName(customerDto.getCustomerName());
                    }
                    return customer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<Void> deleteCustomerById(String customerId) {
        return customerRepository.deleteById(customerId);
    }
}
