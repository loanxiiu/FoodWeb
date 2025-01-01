package com.example.food_web2.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String name;
    String password;
    String email;
    String phone;
    String address;
    String role;
    String image;
}
