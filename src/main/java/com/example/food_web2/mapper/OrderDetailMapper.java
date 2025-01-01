package com.example.food_web2.mapper;

import com.example.food_web2.dto.request.OrderDetailRequest;
import com.example.food_web2.dto.request.OrderRequest;
import com.example.food_web2.dto.response.OrderDetailResponse;
import com.example.food_web2.dto.response.OrderResponse;
import com.example.food_web2.entity.Order;
import com.example.food_web2.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(
            target = "order",
            ignore = true
    )
    OrderDetail toOrderDetail(OrderDetailRequest orderDetailRequest);

    @Mapping(target = "orderId", source = "order.id")
    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);

    @Mapping(
            target = "order",
            ignore = true
    )
    void updateOrderDetail(@MappingTarget OrderDetail orderDetail, OrderDetailRequest orderDetailRequest);
}
