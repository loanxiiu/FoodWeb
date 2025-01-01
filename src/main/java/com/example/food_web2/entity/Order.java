package com.example.food_web2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderDetail> orderDetails = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;
    int deliveryPersonId;
    double totalPrice;
    String status;
    Date orderDate;
    Date deliveryDate;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    Payment payment;
}
