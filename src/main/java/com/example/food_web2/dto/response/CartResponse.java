package com.example.food_web2.dto.response;


import com.example.food_web2.dto.request.CartItemRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartResponse {
    int id;
    CustomerResponse customerResponse;
    List<CartItemResponse> cartItemResponses = new ArrayList<>();
    double totalPrice;
}
