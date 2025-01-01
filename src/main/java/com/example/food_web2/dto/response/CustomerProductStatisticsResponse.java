package com.example.food_web2.dto.response;

public record CustomerProductStatisticsResponse(
        String customerName,    // Name of the customer
        Integer quantity
) {
}
