package com.example.food_web2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String description;
    String images;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    Brand brand;
    int quantity;
    double price;
    String location;
    @OneToMany(mappedBy = "food",cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "food",cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<CartItem> cartItems;
}
