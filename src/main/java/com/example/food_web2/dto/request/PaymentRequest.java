package com.example.food_web2.dto.request;

import com.example.food_web2.entity.Order;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {
    int orderId;
    String paymentMethod;
//    double amount;
    String status;
}
