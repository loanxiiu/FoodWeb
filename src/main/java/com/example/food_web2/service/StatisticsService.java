package com.example.food_web2.service;

import com.example.food_web2.dto.response.BrandStatisticsResponse;
import com.example.food_web2.dto.response.CustomerProductStatisticsResponse;
import com.example.food_web2.dto.response.CustomerStatisticsResponse;
import com.example.food_web2.dto.response.ProductStatisticsResponse;
import com.example.food_web2.entity.Customer;
import com.example.food_web2.repository.CustomerRepository;
import com.example.food_web2.repository.OrderDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticsService {
    OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;

    public List<ProductStatisticsResponse> getProductStatisticsByMonth(YearMonth month) {
        return orderDetailRepository.findProductStatisticsByMonth(month.atDay(1), month.atEndOfMonth());
    }

    public List<CustomerStatisticsResponse> getCustomerStatisticsByMonth(YearMonth month) {
        return orderDetailRepository.findCustomerStatisticsByMonth(month.atDay(1), month.atEndOfMonth());
    }

    public List<CustomerProductStatisticsResponse> getCustomerProductStatisticsByMonth(YearMonth month) {
        List<Object[]> results = orderDetailRepository.findCustomerOrderDetailsByMonth(month.atDay(1), month.atEndOfMonth());
        List<CustomerProductStatisticsResponse> response = new ArrayList<>();

        // Create a map to hold the total quantity for each customer
        Map<Integer, Integer> customerQuantityMap = new HashMap<>();

        // Populate the map with results
        for (Object[] result : results) {
            Integer customerId = (Integer) result[0];  // Customer ID
            Integer quantity = (Integer) result[1];     // Quantity purchased

            // Update the total quantity for the customer
            customerQuantityMap.put(customerId, customerQuantityMap.getOrDefault(customerId, 0) + quantity);
        }

        // Get all customers and create the response
        List<Customer> customers = customerRepository.findAll();
        for (Customer customer : customers) {
            Integer totalQuantity = customerQuantityMap.getOrDefault(customer.getId(), 0);
            response.add(new CustomerProductStatisticsResponse(customer.getUser ().getName(), totalQuantity));
        }

        return response;
    }
    public List<BrandStatisticsResponse> getBrandStatisticsByMonth(YearMonth month) {
        return orderDetailRepository.findBrandStatisticsByMonth(month.atDay(1), month.atEndOfMonth());
    }
}
