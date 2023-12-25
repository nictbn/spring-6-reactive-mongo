package com.example.spring6reactivemongo.mappers;

import com.example.spring6reactivemongo.domain.Customer;
import com.example.spring6reactivemongo.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    CustomerDto customerToCustomerDto(Customer customer);
    Customer customerDtoToCustomer(CustomerDto customerDto);
}
