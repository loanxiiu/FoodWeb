package com.example.food_web2.mapper;

import com.example.food_web2.dto.request.OrderRequest;
import com.example.food_web2.dto.response.OrderResponse;
import com.example.food_web2.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Order toOrder(OrderRequest orderRequest);

    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void updateOrder(@MappingTarget Order order, OrderRequest orderRequest);


}
