package com.example.food_web2.service;

import com.example.food_web2.dto.request.OrderDetailRequest;
import com.example.food_web2.dto.response.OrderDetailResponse;
import com.example.food_web2.entity.Food;
import com.example.food_web2.entity.Order;
import com.example.food_web2.entity.OrderDetail;
import com.example.food_web2.mapper.FoodMapper;
import com.example.food_web2.mapper.OrderDetailMapper;
import com.example.food_web2.mapper.OrderMapper;
import com.example.food_web2.repository.FoodRepository;
import com.example.food_web2.repository.OrderDetailRepository;
import com.example.food_web2.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class OrderDetailService {
    OrderDetailRepository orderDetailRepository;
    OrderDetailMapper orderDetailMapper;
    FoodMapper foodMapper;
    FoodRepository foodRepository;
    OrderRepository orderRepository;

    public OrderDetailResponse create(OrderDetailRequest orderDetailRequest) {
        var orderOptional = orderRepository.findById(orderDetailRequest.getOrderId());
        if(orderOptional.isEmpty()) {
            return null;
        }
        var order = orderOptional.get();
        var foodOptional = foodRepository.findById(orderDetailRequest.getFoodId());
        if(foodOptional.isEmpty()){
            return null;
        }
        var food = foodOptional.get();
        OrderDetail orderDetail = orderDetailMapper.toOrderDetail(orderDetailRequest);
        orderDetail.setFood(food);
        orderDetail.setOrder(order);
        return orderDetailMapper.toOrderDetailResponse(orderDetailRepository.save(orderDetail));
    }

    public OrderDetailResponse findById(int id) {
        var orderDetailOptional = orderDetailRepository.findById(id);
        if(!orderDetailOptional.isEmpty()) {
            OrderDetail orderDetail = orderDetailOptional.get();
            OrderDetailResponse orderDetailResponse = orderDetailMapper.toOrderDetailResponse(orderDetail);
            orderDetailResponse.setFoodResponse(foodMapper.toFoodResponse(orderDetail.getFood()));
            return orderDetailResponse;
        }
        return null;
    }

    public List<OrderDetailResponse> findAll() {
        return orderDetailRepository.findAll().stream().map(orderDetail -> {
            OrderDetailResponse orderDetailResponse = orderDetailMapper.toOrderDetailResponse(orderDetail);
            orderDetailResponse.setFoodResponse(foodMapper.toFoodResponse(orderDetail.getFood()));
            return orderDetailResponse;
        }).toList();
    }

    public List<OrderDetailResponse> findByOrderId(int orderId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        return orderDetails.stream().map(orderDetail -> {
            OrderDetailResponse orderDetailResponse = orderDetailMapper.toOrderDetailResponse(orderDetail);
            orderDetailResponse.setFoodResponse(foodMapper.toFoodResponse(orderDetail.getFood()));
            return orderDetailResponse;
        }).toList();
    }

    public OrderDetailResponse update(int id, OrderDetailRequest orderDetailRequest) {
        var orderDetailOptional = orderDetailRepository.findById(id);
        if(orderDetailOptional.isEmpty()) {
            return null;
        }
        OrderDetail orderDetail = orderDetailOptional.get();
        var foodOptional = foodRepository.findById(orderDetailRequest.getFoodId());
        Food food = foodOptional.get();
        orderDetailMapper.updateOrderDetail(orderDetail, orderDetailRequest);
        orderDetail.setFood(food);
        orderDetailRepository.save(orderDetail);
        return orderDetailMapper.toOrderDetailResponse(orderDetail);
    }

    @Transactional
    public void delete(int id) {
        orderDetailRepository.deleteById(id);
    }
}
