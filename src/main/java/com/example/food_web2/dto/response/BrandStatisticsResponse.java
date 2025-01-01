package com.example.food_web2.dto.response;

public record BrandStatisticsResponse(
        String brandName,       // Name of the brand
        Long totalQuantitySold // Total quantity sold for the brand
) {}
