package com.example.food_web2.dto.response;

public record ProductStatisticsResponse(
        String productName,    // Name of the product
        Long totalQuantitySold // Total quantity sold for the product
) {}
