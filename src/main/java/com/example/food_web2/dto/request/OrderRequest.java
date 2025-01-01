package com.example.food_web2.dto.request;

import com.example.food_web2.entity.Customer;
import com.example.food_web2.entity.OrderDetail;
import com.example.food_web2.entity.Payment;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class OrderRequest {
    List<OrderDetailRequest> orderDetailRequests = new ArrayList<>();
    int customerId;
    int deliveryPersonId;
    double totalPrice;
    String status;
    Date orderDate;
    Date deliveryDate;
}
