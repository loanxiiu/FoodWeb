package com.example.food_web2.mapper;

import com.example.food_web2.dto.request.CustomerRequest;
import com.example.food_web2.dto.response.CustomerResponse;
import com.example.food_web2.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "user", ignore = true)
    Customer toCustomer(CustomerRequest customerRequest);
    CustomerResponse toCustomerResponse(Customer customer);
    @Mapping(target = "orders", ignore = true)
    void updateCustomer(@MappingTarget Customer customer, CustomerRequest customerRequest);
}
