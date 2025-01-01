package com.example.food_web2.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartRequest {
    int customerId;
    List<Integer> cartItems = new ArrayList<>();
    double totalPrice;
}
