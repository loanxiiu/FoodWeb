package com.example.food_web2.service;


import com.example.food_web2.dto.request.CustomerRequest;
import com.example.food_web2.dto.response.CustomerResponse;
import com.example.food_web2.dto.response.OrderDetailResponse;
import com.example.food_web2.dto.response.OrderResponse;
import com.example.food_web2.entity.Customer;
import com.example.food_web2.entity.Order;
import com.example.food_web2.entity.User;
import com.example.food_web2.mapper.*;
import com.example.food_web2.repository.CustomerRepository;
import com.example.food_web2.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Transactional
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    UserRepository userRepository;
    EntityManager entityManager;
    UserMapper userMapper;
    OrderDetailMapper orderDetailMapper;
    PaymentMapper paymentMapper;
    FoodMapper foodMapper;

    @Transactional
    public void resetAutoIncrement() {
        entityManager.createNativeQuery("ALTER TABLE food_web.customer AUTO_INCREMENT = 1").executeUpdate();
    }

    @Transactional
    public List<CustomerResponse> create() {
        // Fetch all users with the role "Customer"
        resetAutoIncrement();
        List<Customer> customers = customerRepository.findAll();
        List<User> users = userRepository.findAllByRole("Customer");
        List<User> newUsers = users.stream()
                .filter(user -> customers.stream()
                        .noneMatch(customer -> customer.getUser().getId() == user.getId()))
                .toList();
        if (!newUsers.isEmpty()) {
            return newUsers.stream()
                    .map(user -> {
                        // Convert User to Customer entity
                        Customer customer = new Customer();
                        customer.setUser(user);
                        // Save the customer entity
                        Customer savedCustomer = customerRepository.save(customer);
                        // Convert the saved Customer to CustomerResponse
                        return customerMapper.toCustomerResponse(savedCustomer);
                    })
                    .toList();
        }
        return Collections.emptyList();
    }

    // miss
    @Transactional
    public CustomerResponse update(int id, CustomerRequest customerRequest) {
        var customerOptional = customerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            return null;
        }
        Customer customer = customerOptional.get();
        customerMapper.updateCustomer(customer, customerRequest);
        customer = customerRepository.save(customer);
        return mapCustomer(customer);
    }

    public CustomerResponse findByUserId(int userId) {
        return customerMapper.toCustomerResponse(customerRepository.findByUserId(userId));
    }

    @Transactional
    public CustomerResponse findById(int id) {
        var customerOptional = customerRepository.findById(id);
        if (!customerOptional.isEmpty()) {
            Customer customer = customerOptional.get();
            return mapCustomer(customer);
        }
        return null;
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream().map(this::mapCustomer).toList();
    }

    public CustomerResponse mapCustomer(Customer customer) {
        CustomerResponse customerResponse = customerMapper.toCustomerResponse(customer);
        customerResponse.setUserResponse(userMapper.toUserResponse(customer.getUser()));
        customerResponse.setOrderHistory(mapOrders(customer.getOrders()));
        return customerResponse;
    }


    public List<OrderResponse> mapOrders(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return new ArrayList<>();
        }
        return orders.stream()
                .map(order -> {
                    OrderResponse orderResponse = new OrderResponse();
                    List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream()
                            .map(orderDetail -> {
                                OrderDetailResponse orderDetailResponse = orderDetailMapper.toOrderDetailResponse(orderDetail);
                                orderDetailResponse.setFoodResponse(foodMapper.toFoodResponse(orderDetail.getFood()));
                                return orderDetailResponse;
                            })
                            .toList();
                    orderResponse.setOrderDetailResponses(orderDetailResponses);
                    orderResponse.setCustomerResponse(customerMapper.toCustomerResponse(order.getCustomer()));
                    orderResponse.setDeliveryPersonId(order.getDeliveryPersonId());
                    orderResponse.setTotalPrice(order.getTotalPrice());
                    orderResponse.setStatus(order.getStatus());
                    order.setOrderDate(order.getOrderDate());
                    order.setDeliveryDate(order.getDeliveryDate());
                    orderResponse.setPaymentResponse(paymentMapper.toPaymentResponse(order.getPayment()));
                    return orderResponse;
                }).toList();
    }
}
