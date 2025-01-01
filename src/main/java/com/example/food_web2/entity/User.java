package com.example.food_web2.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    String password;
    String email;
    String phone;
    String address;
    String role;
    String image;
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)// Foreign key column
    Customer customer;
}
