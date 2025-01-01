package com.example.food_web2.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int id;
    String name;
//    String password;
    String email;
    String phone;
    String address;
    String image;
    String role;
}
