package com.example.food_web2.service;

import com.example.food_web2.dto.request.PaymentRequest;
import com.example.food_web2.dto.response.*;
import com.example.food_web2.entity.Order;
import com.example.food_web2.entity.OrderDetail;
import com.example.food_web2.entity.Payment;
import com.example.food_web2.mapper.*;
import com.example.food_web2.repository.OrderRepository;
import com.example.food_web2.repository.PaymentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class PaymentService {
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;
    OrderRepository orderRepository;
    OrderDetailMapper orderDetailMapper;
    FoodMapper foodMapper;
    OrderMapper orderMapper;
    CustomerMapper customerMapper;
    UserMapper userMapper;

    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll().stream().map(this::mapPayment).toList();
    }

    public PaymentResponse findById(int id) {
        var paymentOptional = paymentRepository.findById(id);
        Payment payment = paymentOptional.get();
        var orderOptional = orderRepository.findById(payment.getOrder().getId());
        payment.setOrder(orderOptional.get());
        payment.setAmount(orderOptional.get().getTotalPrice());
        return mapPayment(payment);
    }

    public PaymentResponse findByOrderId(int orderId) {
        var paymentOptional = paymentRepository.findByOrderId(orderId);
        Payment payment = paymentOptional.get();
        var orderOptional = orderRepository.findById(payment.getOrder().getId());
        payment.setOrder(orderOptional.get());
        payment.setAmount(orderOptional.get().getTotalPrice());
        return mapPayment(payment);
    }

    public PaymentResponse create(PaymentRequest paymentRequest) {
        paymentRequest.setStatus("Chưa thanh toán");
        var orderOptional = orderRepository.findById(paymentRequest.getOrderId());
        Order order = orderOptional.get();
        order.setStatus("Chờ xác nhận");
        orderRepository.save(order);
        Payment payment = paymentMapper.toPayment(paymentRequest);
        payment.setOrder(order);
        payment.setAmount(orderOptional.get().getTotalPrice());
        paymentRepository.save(payment);
        return mapPayment(payment);
    }

    public PaymentResponse update(int id, PaymentRequest paymentRequest) {
        var paymentOptional = paymentRepository.findById(id);
        Payment payment = paymentOptional.get();
        var orderOptional = orderRepository.findById(paymentRequest.getOrderId());
        payment.setOrder(orderOptional.get());
        payment.setAmount(orderOptional.get().getTotalPrice());
        paymentMapper.updatePayment(payment, paymentRequest);
        return paymentMapper.toPaymentResponse(paymentRepository.save(payment));
    }


    //@OneToOne ko xóa được 1 cái
//    @Transactional
//    public void delete(int id) {
//        paymentRepository.deleteById(id);
//    }

    @Transactional
    public void delete(int id) {
        var paymentOptional = paymentRepository.findById(id);
        if (paymentOptional.isPresent()) {
            Payment payment = paymentOptional.get();

            // Break the relationship with the associated Order entity
            if (payment.getOrder() != null) {
                payment.getOrder().setPayment(null);
                orderRepository.save(payment.getOrder()); // Save the updated Order
            }

            // Delete the Payment entity
            paymentRepository.delete(payment);
        } else {
            log.warn("Payment not found with ID: {}", id);
        }
    }

    @Transactional
    public void deleteAll() {
        List<Payment> payments = paymentRepository.findAll();

        // Break relationships with Order entities
        payments.forEach(payment -> {
            if (payment.getOrder() != null) {
                payment.getOrder().setPayment(null);
                orderRepository.save(payment.getOrder());
            }
        });

        // Delete all Payment entities
        paymentRepository.deleteAll();
    }


    private PaymentResponse mapPayment(Payment payment) {
        List<OrderDetailResponse> orderDetailResponses = payment.getOrder().getOrderDetails().stream()
                .map(orderDetail -> {
                    OrderDetailResponse orderDetailResponse = orderDetailMapper.toOrderDetailResponse(orderDetail);
                    orderDetailResponse.setFoodResponse(foodMapper.toFoodResponse(orderDetail.getFood()));
                    return orderDetailResponse;
                })
                .toList();
        PaymentResponse paymentResponse = paymentMapper.toPaymentResponse(payment);
        OrderResponse orderResponse = orderMapper.toOrderResponse(payment.getOrder());
        orderResponse.setOrderDetailResponses(orderDetailResponses);
        UserResponse userResponse = userMapper.toUserResponse(payment.getOrder().getCustomer().getUser());
        CustomerResponse customerResponse = customerMapper.toCustomerResponse(payment.getOrder().getCustomer());
        customerResponse.setUserResponse(userResponse);
        orderResponse.setCustomerResponse(customerResponse);
        paymentResponse.setOrderResponse(orderResponse);
        return paymentResponse;
    }


    //Doanh thu
    public List<Double> getRevenueForMonth(int month, int year) {
        // Lấy ngày đầu tiên và ngày cuối cùng của tháng hiện tại
        LocalDate startOfMonth = LocalDate.of(year, month, 1); // Ngày đầu tháng
        LocalDate endOfMonth = LocalDate.of(year, month, startOfMonth.lengthOfMonth()); // Ngày cuối tháng

        // Lấy tất cả các đơn hàng trong năm và tháng hiện tại
        List<Order> orders = orderRepository.findAllByYearAndMonth(year, month);

        // Tạo một map để lưu doanh thu theo từng ngày
        Map<LocalDate, Double> revenueByDay = new HashMap<>();

        for (Order order : orders) {
            if (order.getStatus() != null && order.getStatus().equalsIgnoreCase("Giao hàng thành công")) {
                // Chỉ tính đơn hàng đã giao thành công
                LocalDate orderDate = order.getOrderDate().toLocalDate(); // Chuyển đổi java.sql.Date -> LocalDate
                double orderRevenue = order.getTotalPrice();
                revenueByDay.put(orderDate, revenueByDay.getOrDefault(orderDate, 0.0) + orderRevenue);
            }
        }

        // Tạo danh sách doanh thu cho tất cả các ngày trong tháng (kể cả ngày không có đơn hàng)
        List<Double> revenueList = new ArrayList<>();

        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            revenueList.add(revenueByDay.getOrDefault(date, 0.0)); // Nếu không có đơn hàng thì doanh thu = 0
        }

        return revenueList;
    }

    public List<Double> getRevenueForYear(int year) {
        // Tạo danh sách chứa doanh thu của từng tháng trong năm
        List<Double> revenueList = new ArrayList<>();

        // Lặp qua tất cả các tháng trong năm
        for (int month = 1; month <= 12; month++) {
            // Lấy ngày đầu tiên và ngày cuối cùng của tháng hiện tại
            LocalDate startOfMonth = LocalDate.of(year, month, 1); // Ngày đầu tháng
            LocalDate endOfMonth = LocalDate.of(year, month, startOfMonth.lengthOfMonth()); // Ngày cuối tháng

            // Lấy tất cả các đơn hàng trong năm và tháng hiện tại
            List<Order> orders = orderRepository.findAllByYearAndMonth(year, month);

            // Tạo một map để lưu doanh thu theo từng ngày
            Map<LocalDate, Double> revenueByDay = new HashMap<>();

            for (Order order : orders) {
                if (order.getStatus() != null && order.getStatus().equalsIgnoreCase("Giao hàng thành công")) {
                    // Chỉ tính đơn hàng đã giao thành công
                    LocalDate orderDate = order.getOrderDate().toLocalDate(); // Chuyển đổi java.sql.Date -> LocalDate
                    double orderRevenue = order.getTotalPrice();
                    revenueByDay.put(orderDate, revenueByDay.getOrDefault(orderDate, 0.0) + orderRevenue);
                }
            }

            // Tính tổng doanh thu cho tháng này
            double totalRevenueForMonth = 0.0;
            for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
                totalRevenueForMonth += revenueByDay.getOrDefault(date, 0.0); // Nếu không có đơn hàng thì doanh thu = 0
            }

            // Thêm doanh thu của tháng này vào danh sách
            revenueList.add(totalRevenueForMonth);
        }

        return revenueList;
    }


    // Revenue for a specific day
    public double getRevenueByDay(LocalDate date) {
        return orderRepository.findByOrderDate(Date.valueOf(date)).stream()
                .filter(order -> "Giao hàng thành công".equals(order.getStatus()))
                .mapToDouble(order -> order.getTotalPrice())
                .sum();
    }

    // Revenue for a specific month
    public double getRevenueByMonth(int year, int month) {
        return orderRepository.findAll().stream()
                .filter(order -> {
                    LocalDate orderDate = order.getOrderDate().toLocalDate();
                    return orderDate.getYear() == year && orderDate.getMonthValue() == month
                            && "Giao hàng thành công".equals(order.getStatus());
                })
                .mapToDouble(order -> order.getTotalPrice())
                .sum();
    }

    // Revenue for a specific year
    public double getRevenueByYear(int year) {
        return orderRepository.findAll().stream()
                .filter(order -> {
                    LocalDate orderDate = order.getOrderDate().toLocalDate();
                    return orderDate.getYear() == year && "Giao hàng thành công".equals(order.getStatus());
                })
                .mapToDouble(order -> order.getTotalPrice())
                .sum();
    }

    // Group revenue by months for a specific year
    public Map<Integer, Double> getMonthlyRevenueByYear(int year) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderDate().toLocalDate().getYear() == year &&
                        "Giao hàng thành công".equals(order.getStatus()))
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().toLocalDate().getMonthValue(),
                        Collectors.summingDouble(order -> order.getTotalPrice())
                ));
    }
}
