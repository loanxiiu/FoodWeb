package com.example.food_web2.service;

import com.example.food_web2.dto.request.OrderRequest;
import com.example.food_web2.dto.response.CustomerResponse;
import com.example.food_web2.dto.response.OrderDetailResponse;
import com.example.food_web2.dto.response.OrderResponse;
import com.example.food_web2.entity.*;
import com.example.food_web2.mapper.*;
import com.example.food_web2.repository.CustomerRepository;
import com.example.food_web2.repository.FoodRepository;
import com.example.food_web2.repository.OrderRepository;
import com.example.food_web2.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    OrderDetailMapper orderDetailMapper;
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    FoodRepository foodRepository;
    FoodMapper foodMapper;
    UserRepository userRepository;
    UserMapper userMapper;

    public OrderResponse create(OrderRequest orderRequest) {
        // Convert orderRequest to order entity
        Order order = orderMapper.toOrder(orderRequest);

        Random random = new Random();
        order.setDeliveryPersonId(random.nextInt(20)+1);

        // Set current order date (current time)
        order.setOrderDate(new java.sql.Date(System.currentTimeMillis())); // for current time
        order.setDeliveryDate(new java.sql.Date(System.currentTimeMillis()));

        // Map order details and associate them with the order
        order.setOrderDetails(
                orderRequest.getOrderDetailRequests().stream()
                        .map(orderDetailRequest -> {
                            var foodOptional = foodRepository.findById(orderDetailRequest.getFoodId());
                            Food food = foodOptional.get();
                            OrderDetail orderDetail = orderDetailMapper.toOrderDetail(orderDetailRequest);
                            double price = food.getPrice()*orderDetail.getQuantity();
                            orderDetail.setPrice(price);
                            orderDetail.setFood(food);
                            orderDetail.setOrder(order); // Set parent relationship
                            return orderDetail;
                        }).toList()
        );

        // Set customer for the order
        Customer customer = customerRepository.findByUserId(orderRequest.getCustomerId());
        order.setCustomer(customer);
        order.setTotalPrice(20000+order.getOrderDetails().stream().map(orderDetail -> orderDetail.getPrice()).reduce(0.0, Double::sum));
        // Save order to the database
        Order savedOrder = orderRepository.save(order);

        return mapOrder(savedOrder);
    }

    public OrderResponse findById(int id) {
        var orderOptional = orderRepository.findById(id);
        Order savedOrder = orderOptional.get();
        return mapOrder(savedOrder);
    }

    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAll();
        if(orders.isEmpty() || orders ==null){
            return null;
        }
        return orders.stream().map(this::mapOrder).toList();
    }

    public List<OrderResponse> findByCustomerId(int customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream().map(this::mapOrder).toList();
    }

    public OrderResponse update(int id, OrderRequest orderRequest) {
        var orderOptional = orderRepository.findById(id);
        Order order = orderOptional.get();
        orderMapper.updateOrder(order,orderRequest);
        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }
    public void delete(int id) {
        orderRepository.deleteById(id);
    }

    public void deleteAll(){
        orderRepository.deleteAll();
    }

    public List<OrderDetailResponse> mapOrderDetails(List<OrderDetail> orderDetails){
        if(orderDetails == null || orderDetails.isEmpty()) return null;
        return orderDetails.stream().map(orderDetail -> {
            OrderDetailResponse orderDetailResponse = orderDetailMapper.toOrderDetailResponse(orderDetail);
            orderDetailResponse.setFoodResponse(foodMapper.toFoodResponse(orderDetail.getFood()));
            return orderDetailResponse;
        }).collect(Collectors.toList());
    }

    public OrderResponse mapOrder(Order order) {
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        var customerOptional = customerRepository.findById(order.getCustomer().getId());
        Customer customer = customerOptional.get();
        orderResponse.setOrderDetailResponses(mapOrderDetails(order.getOrderDetails()));
        CustomerResponse customerResponse = customerMapper.toCustomerResponse(customer);
        customerResponse.setUserResponse(userMapper.toUserResponse(order.getCustomer().getUser()));
        orderResponse.setCustomerResponse(customerResponse);
//        double totalPrice = orderResponse.getOrderDetailResponses().stream()
//                .mapToDouble(orderDetail -> orderDetail.getFoodResponse().getPrice() * orderDetail.getQuantity())
//                .sum();
        orderResponse.setTotalPrice(order.getTotalPrice());
        return orderResponse;
    }
}
