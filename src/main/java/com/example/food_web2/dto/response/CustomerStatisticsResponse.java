package com.example.food_web2.dto.response;

public record CustomerStatisticsResponse(
        String customerName,    // Name of the customer
        Double totalSpent       // Total amount spent by the customer
) {}
