package com.example.food_web2.controller;

import com.example.food_web2.dto.response.BrandStatisticsResponse;
import com.example.food_web2.dto.response.CustomerProductStatisticsResponse;
import com.example.food_web2.dto.response.CustomerStatisticsResponse;
import com.example.food_web2.dto.response.ProductStatisticsResponse;
import com.example.food_web2.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
//@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/admin/products/chart")
    public List<ProductStatisticsResponse> getProductStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return statisticsService.getProductStatisticsByMonth(month);
    }

    @GetMapping("/admin/customers/chart")
    public List<CustomerStatisticsResponse> getCustomerStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return statisticsService.getCustomerStatisticsByMonth(month);
    }

    @GetMapping("/admin/customers/quantity/chart")
    public List<CustomerProductStatisticsResponse> getCustomerProductStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return statisticsService.getCustomerProductStatisticsByMonth(month);
    }

    @GetMapping("/admin/brands/chart")
    public List<BrandStatisticsResponse> getBrandStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return statisticsService.getBrandStatisticsByMonth(month);
    }

}
