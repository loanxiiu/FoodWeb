package com.example.food_web2.controller;

import com.example.food_web2.dto.request.CartRequest;
import com.example.food_web2.dto.response.CartItemResponse;
import com.example.food_web2.dto.response.CartResponse;
import com.example.food_web2.dto.response.UserResponse;
import com.example.food_web2.service.CartService;
import com.example.food_web2.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
//@RestController
//@RequestMapping("/carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;
    CustomerService customerService;

        @GetMapping
    String cartPage(HttpSession session, Model model){
        UserResponse user = (UserResponse) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/dangnhap"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        var customer = customerService.findByUserId(user.getId());
        CartResponse cartResponse = cartService.findByCustomerId(customer.getId());
        model.addAttribute("cart", cartResponse);
        return "/user/cart/giohang";
    }
//    @GetMapping
//    @ResponseBody // Ensure this method returns JSON
//    public List<CartItemResponse> findAll(HttpSession session) {
//        UserResponse user = (UserResponse) session.getAttribute("user");
//        if (user == null) {
//            return null; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
//        }
//        var customer = customerService.findByUserId(user.getId());
//        return cartService.findByCustomerId(customer.getId()).getCartItemResponses();
//    }

    @GetMapping("/getAll")
    public List<CartResponse> findAll() {
        return cartService.findAll();
    }

    @GetMapping("/{id}")
    public CartResponse findById(@PathVariable int id) {
        return cartService.findById(id);
    }

    @GetMapping("/customer/{id}")
    public CartResponse findByCustomerId(@PathVariable int id) {
        return cartService.findByCustomerId(id);
    }

    @PostMapping
    public List<CartResponse> create() {
        return cartService.create();
    }

    @PutMapping("/{id}")
    public CartResponse update(@PathVariable int id, @RequestBody CartRequest cartRequest) {
        return cartService.update(id, cartRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        cartService.delete(id);
    }
}
