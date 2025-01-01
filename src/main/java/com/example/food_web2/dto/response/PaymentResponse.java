package com.example.food_web2.dto.response;

import com.example.food_web2.entity.Order;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    int id;
    OrderResponse orderResponse;
    String paymentMethod;
    double amount;
    String status;
}
