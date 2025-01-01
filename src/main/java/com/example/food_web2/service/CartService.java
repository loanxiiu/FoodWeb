package com.example.food_web2.service;

import com.example.food_web2.dto.request.CartRequest;
import com.example.food_web2.dto.response.CartItemResponse;
import com.example.food_web2.dto.response.CartResponse;
import com.example.food_web2.dto.response.CustomerResponse;
import com.example.food_web2.dto.response.FoodResponse;
import com.example.food_web2.entity.Cart;
import com.example.food_web2.entity.CartItem;
import com.example.food_web2.entity.Customer;
import com.example.food_web2.entity.User;
import com.example.food_web2.mapper.*;
import com.example.food_web2.repository.CartRepository;
import com.example.food_web2.repository.CustomerRepository;
import com.example.food_web2.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartService {
    private final CartItemMapper cartItemMapper;
    CartRepository cartRepository;
    CartMapper cartMapper;
    CustomerRepository customerRepository;
    UserRepository userRepository;
    CustomerMapper customerMapper;
    UserMapper userMapper;
    FoodMapper foodMapper;

    public List<CartResponse> create() {
        List<Customer> customers = customerRepository.findAll();
        List<Cart> carts = cartRepository.findAll();
        List<Customer> newCustomers = customers.stream()
                .filter(customer -> carts.stream()
                        .noneMatch(cart -> customer.getId() == cart.getCustomer().getId()))
                .toList();
        if (!newCustomers.isEmpty()) {
            return newCustomers.stream()
                    .map(customer -> {
                        // Convert User to Customer entity
                        Cart cart = new Cart();
                        cart.setCustomer(customer);
                        cartRepository.save(cart);
                        CartResponse cartResponse = cartMapper.toCardResponse(cart);
                        cartResponse.setCustomerResponse(mapCustomer(customer));
                        return cartResponse;
                    })
                    .toList();
        }
        return Collections.emptyList();
    }

    public List<CartResponse> findAll() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream().map(this::mapCart).toList();
    }

    public CartResponse findById(int id) {
        var cartOptional = cartRepository.findById(id);
        Cart cart = cartOptional.get();
        return mapCart(cart);
    }

    public CartResponse findByCustomerId(int customerId) {
        var cartOptional = cartRepository.findByCustomerId(customerId);
        Cart cart = cartOptional.get();
        return mapCart(cart);
    }

    public CartResponse update(int id, CartRequest cartRequest) {
        var cartOptional = cartRepository.findById(id);
        Cart cart = cartOptional.get();
        cartMapper.updateCart(cart, cartRequest);
        cartRepository.save(cart);
        return mapCart(cart);
    }

    public void delete(int id) {
        cartRepository.deleteById(id);
    }

    private CustomerResponse mapCustomer(Customer customer) {
        CustomerResponse customerResponse = customerMapper.toCustomerResponse(customer);
        customerResponse.setUserResponse(userMapper.toUserResponse(customer.getUser()));
        return customerResponse;
    }

    private CartResponse mapCart(Cart cart) {
        CartResponse cartResponse = cartMapper.toCardResponse(cart);
        cartResponse.setId(cart.getId());
        CustomerResponse customerResponse = customerMapper.toCustomerResponse(cart.getCustomer());
        customerResponse.setUserResponse(userMapper.toUserResponse(cart.getCustomer().getUser()));
        cartResponse.setCustomerResponse(customerResponse);
        cartResponse.setCartItemResponses(cart.getCartItems().stream()
                .map(cartItem -> {
                    CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItem);
                    cartItemResponse.setFoodResponse(foodMapper.toFoodResponse(cartItem.getFood()));
                    return cartItemResponse;
                }).toList());
        double totalPrice = cartResponse.getCartItemResponses().stream()
                .mapToDouble(cartItem -> cartItem.getFoodResponse().getPrice() * cartItem.getQuantity())
                .sum();
        cartResponse.setTotalPrice(totalPrice);
        return cartResponse;
    }

}
