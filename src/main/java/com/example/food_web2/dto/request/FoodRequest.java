package com.example.food_web2.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodRequest {
    String name;
    String description;
    String images;
    String brandName;
    int quantity;
    double price;
    String location;
}
