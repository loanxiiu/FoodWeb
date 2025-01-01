package com.example.food_web2.mapper;

import com.example.food_web2.dto.request.FoodRequest;
import com.example.food_web2.dto.response.FoodResponse;
import com.example.food_web2.entity.Food;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    Food toFood(FoodRequest foodRequest);

    @Mapping(target = "brandName", source = "brand.name")
    FoodResponse toFoodResponse(Food food);

    @Mapping(target = "brand", ignore = true)
    void updateFood(@MappingTarget Food food, FoodRequest foodRequest);

    @Named("mapFoods")
    default List<FoodResponse> mapFoods(List<Food> foods) {
        if (foods == null) {
            return new ArrayList<>();
        }
        return foods.stream()
                .map(food -> {
                    FoodResponse foodResponse = new FoodResponse();
                    foodResponse.setName(food.getName());
                    foodResponse.setDescription(food.getDescription());
                    foodResponse.setImages(food.getImages());
                    foodResponse.setBrandName(food.getBrand().getName());
                    foodResponse.setQuantity(food.getQuantity());
                    foodResponse.setPrice(food.getPrice());
                    foodResponse.setLocation(food.getLocation());
                    return foodResponse;
                })
                .collect(Collectors.toList());
    }
}
