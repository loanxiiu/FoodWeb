package com.example.food_web2.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryResponse {
    int id;
    OrderResponse orderResponse;
    int deliveryPersonId;
    String status;
    Date deliveryDate;
}
