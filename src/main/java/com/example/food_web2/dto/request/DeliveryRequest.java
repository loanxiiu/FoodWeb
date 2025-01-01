package com.example.food_web2.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryRequest {
    int orderId;
    int deliveryPersonId;
    String status;
    Date deliveryDate;
}
