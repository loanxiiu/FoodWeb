package com.example.food_web2.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    int id;
    int cartId;
    FoodResponse foodResponse;
    int quantity;
}
