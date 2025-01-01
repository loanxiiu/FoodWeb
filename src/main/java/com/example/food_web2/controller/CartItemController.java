package com.example.food_web2.controller;

import com.example.food_web2.dto.request.CartItemRequest;
import com.example.food_web2.dto.request.CartRequest;
import com.example.food_web2.dto.response.CartItemResponse;
import com.example.food_web2.dto.response.CartResponse;
import com.example.food_web2.dto.response.CustomerResponse;
import com.example.food_web2.dto.response.UserResponse;
import com.example.food_web2.entity.Cart;
import com.example.food_web2.entity.CartItem;
import com.example.food_web2.entity.User;
import com.example.food_web2.repository.CustomerRepository;
import com.example.food_web2.service.CartItemService;
import com.example.food_web2.service.CartService;
import com.example.food_web2.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("")
//@RestController
//@RequestMapping("/cartItems")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemController {
    private static final Logger log = LoggerFactory.getLogger(CartItemController.class);
    CartItemService cartItemService;
    CustomerRepository customerRepository;
    CartService cartService;
    private final CustomerService customerService;

    @GetMapping("/cartItem")
    public List<CartItemResponse> findAll() {
        return cartItemService.findAll();
    }

    @GetMapping("/cartItem/{id}")
    public CartItemResponse findById(@PathVariable int id) {
        return cartItemService.findById(id);
    }

    @GetMapping("/cartItem/cart/{id}")
    public List<CartItemResponse> findByCartId(@PathVariable int id) {
        return cartItemService.findByCartId(id);
    }

    //ajax
    @PostMapping("/cartItem")
    @ResponseBody// Ensure JSON response
    public CartItemResponse create(@RequestBody CartItemRequest cartItemRequest, HttpSession session) {
        UserResponse userResponse = (UserResponse) session.getAttribute("user");
        CustomerResponse customerResponse = customerService.findByUserId(userResponse.getId());
        CartResponse cartResponse = cartService.findByCustomerId(customerResponse.getId());
        cartItemRequest.setCartId(cartResponse.getId());
        CartItemResponse cartItemResponse = cartItemService.create(cartItemRequest);
        return cartItemResponse;
    }


    @PostMapping("/cart/cartItem/update/{id}")
    @ResponseBody // Đảm bảo trả về JSON
    public ResponseEntity<String> updateQuantity(@PathVariable("id") int id,
            @RequestBody CartItemRequest cartItemRequest) {
        // Gọi service để cập nhật
        cartItemService.update(id, cartItemRequest);

        // Trả về kết quả thành công
        return ResponseEntity.ok("Cập nhật thành công!");
    }

//    @PostMapping("/update")
//    public String updateQuantity(
//            @RequestParam("cartItemId") int cartItemId,
//            @RequestParam("cartId") int cartId,
//            @RequestParam("quantity") int quantity) {
//
//        // Tạo request object
//        CartItemRequest cartItemRequest = new CartItemRequest();
//        cartItemRequest.setQuantity(quantity);
//        cartItemRequest.setCartId(cartId);
//        // Gọi service để cập nhật
//        cartItemService.update(cartItemId, cartItemRequest);
//        // Điều hướng về trang giỏ hàng sau khi cập nhật
//        return "redirect:/cart";
//    }

    @GetMapping("/cartItem/delete")
    public String delete(@RequestParam(value = "id") int id) {
        cartItemService.delete(id);
        return "redirect:/cart"; // Điều hướng lại trang giỏ hàng
    }


    @DeleteMapping("/cartItem/deleteAll")
    public void deleteAll() {
        cartItemService.deleteAll();
    }
}
