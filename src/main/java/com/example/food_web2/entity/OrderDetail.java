package com.example.food_web2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})  // Avoid REMOVE cascade here
    @JoinColumn(name = "orders_id")  // Foreign key reference to the Order table
    Order order;

    @ManyToOne
    @JoinColumn(name = "food_id")
    Food food;

    int quantity;
    double price;
}
