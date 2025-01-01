package com.example.food_web2.mapper;

import com.example.food_web2.dto.request.CartRequest;
import com.example.food_web2.dto.response.CartResponse;
import com.example.food_web2.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    Cart toCard(CartRequest cartRequest);
    CartResponse toCardResponse(Cart cart);
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    void updateCart(@MappingTarget Cart cart, CartRequest cartRequest);
}
