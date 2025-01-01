package com.example.food_web2.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @OneToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    User user;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    List<Order> orders = new ArrayList<>();
    @OneToOne(mappedBy = "customer")
    Cart cart;

}
