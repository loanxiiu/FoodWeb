package com.example.food_web2.mapper;

import com.example.food_web2.dto.request.DeliveryRequest;
import com.example.food_web2.dto.response.DeliveryResponse;
import com.example.food_web2.entity.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    @Mapping(target = "order", ignore = true)
    Delivery toDelivery(DeliveryRequest deliveryRequest);
    DeliveryResponse toDeliveryResponse(Delivery delivery);

    @Mapping(target = "order", ignore = true)
    void updateDelivery(@MappingTarget Delivery delivery, DeliveryRequest deliveryRequest);
}
