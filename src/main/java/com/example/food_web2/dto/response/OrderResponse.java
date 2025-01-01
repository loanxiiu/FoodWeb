package com.example.food_web2.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    int id;
    List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
    CustomerResponse customerResponse;
    int deliveryPersonId;
    double totalPrice;
    String status;
    Date orderDate;
    Date deliveryDate;
    PaymentResponse paymentResponse;
}
