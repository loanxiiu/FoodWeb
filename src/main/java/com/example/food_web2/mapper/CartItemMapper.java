package com.example.food_web2.mapper;

import com.example.food_web2.dto.request.CartItemRequest;
import com.example.food_web2.dto.response.CartItemResponse;
import com.example.food_web2.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface CartItemMapper {
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "food", ignore = true)
    CartItem toCartItem(CartItemRequest cartItemRequest);

    @Mapping(target = "cartId", source = "cart.id")
    CartItemResponse toCartItemResponse(CartItem cartItem);

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "food", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    void updateCartItem(@MappingTarget CartItem cartItem, CartItemRequest cartItemRequest);
}
