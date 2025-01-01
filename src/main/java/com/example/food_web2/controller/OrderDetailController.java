package com.example.food_web2.controller;

import com.example.food_web2.dto.request.OrderDetailRequest;
import com.example.food_web2.dto.response.OrderDetailResponse;
import com.example.food_web2.dto.response.UserResponse;
import com.example.food_web2.service.OrderDetailService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RestController
//@RequestMapping("/orderDetails")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderDetailController {
    OrderDetailService orderDetailService;

    @GetMapping("/orderDetail/getAll")
    List<OrderDetailResponse> findAll() {
        return orderDetailService.findAll();
    }

    @GetMapping("/orderDetail/{id}")
    OrderDetailResponse findById(@PathVariable int id) {
        return orderDetailService.findById(id);
    }

    @GetMapping({"/admin/orderDetail/{id}","/user/orderDetail/{id}"})
    public String findByOrderId(@PathVariable int id, Model model, HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute("user");
        if (user != null) {
            if(user.getRole().equals("Admin")) {
                model.addAttribute("orderId", id);
                model.addAttribute("orderDetails", orderDetailService.findByOrderId(id));
                return "/admin/order/chitietdh";
            }else {
                model.addAttribute("orderId", id);
                model.addAttribute("orderDetails", orderDetailService.findByOrderId(id));
                return "/user/order/chitietdh";
            }
        }
        return "/user/dangnhap/dangnhap";
    }

    @PostMapping("/orderDetail")
    OrderDetailResponse create(@RequestBody OrderDetailRequest orderDetailRequest) {
        return orderDetailService.create(orderDetailRequest);
    }

    @PutMapping("/orderDetail/{id}")
    OrderDetailResponse update(@PathVariable int id, @RequestBody OrderDetailRequest orderDetailRequest) {
        return orderDetailService.update(id,orderDetailRequest);
    }

    @DeleteMapping("/orderDetail/{id}")
    void delete(@PathVariable int id) {
        orderDetailService.delete(id);
    }
}
