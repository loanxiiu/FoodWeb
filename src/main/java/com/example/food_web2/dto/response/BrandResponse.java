package com.example.food_web2.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandResponse {
    int id;
    String name;
    List<FoodResponse> foods = new ArrayList<>();
}
