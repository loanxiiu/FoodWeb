package com.example.food_web2.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailRequest {
    int orderId;
    int foodId;
//    FoodRequest foodRequest;
    int quantity;
    double price;
}
