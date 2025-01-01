package com.example.food_web2.service;

import com.example.food_web2.dto.request.CartItemRequest;
import com.example.food_web2.dto.response.CartItemResponse;
import com.example.food_web2.dto.response.CartResponse;
import com.example.food_web2.entity.Cart;
import com.example.food_web2.entity.CartItem;
import com.example.food_web2.entity.Food;
import com.example.food_web2.mapper.CartItemMapper;
import com.example.food_web2.mapper.FoodMapper;
import com.example.food_web2.repository.CartItemRepository;
import com.example.food_web2.repository.CartRepository;
import com.example.food_web2.repository.CustomerRepository;
import com.example.food_web2.repository.FoodRepository;
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
public class CartItemService {
    CartItemRepository cartItemRepository;
    CartItemMapper cartItemMapper;
    FoodRepository foodRepository;
    CartRepository cartRepository;
    FoodMapper foodMapper;
    CustomerRepository customerRepository;

    public CartItemResponse create(CartItemRequest cartItemRequest) {//nhan vao id user thay cho cart
        var foodOptional = foodRepository.findById(cartItemRequest.getFoodId());
        Food food = foodOptional.get();
        var cartOptional = cartRepository.findById(cartItemRequest.getCartId());
        var cartIt = cartItemMapper.toCartItem(cartItemRequest);
        Cart cart = cartOptional.orElse(null);
        List<CartItem> newCartItems = cart.getCartItems().stream()
                .filter(cartItem -> food.getId() == cartItem.getFood().getId()).toList();
        if(!newCartItems.isEmpty()) {
            CartItem cartItem = newCartItems.get(0);
            cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());
            return mapCartItem(cartItemRepository.save(cartItem));
        }
        cartIt.setFood(foodOptional.orElse(null));
        cartIt.setCart(cartOptional.orElse(null));
        cartIt.setQuantity(cartItemRequest.getQuantity());
        return mapCartItem(cartItemRepository.save(cartIt));
    }

    public List<CartItemResponse> findAll() {
        List<CartItem> cartItemList = cartItemRepository.findAll();
        return cartItemList.stream().map(this::mapCartItem).toList();
    }
    public CartItemResponse findById(int id) {
        var cartItemOptional = cartItemRepository.findById(id);
        CartItem cartItem = cartItemOptional.get();
        return mapCartItem(cartItem);
    }

    public List<CartItemResponse> findByCartId(int cartId) {
        List<CartItem> cartItemList = cartItemRepository.findByCartId(cartId);
        return cartItemList.stream().map(this::mapCartItem).toList();
    }

    public CartItemResponse update(int id, CartItemRequest cartItemRequest) {
        var foodOptional = foodRepository.findById(cartItemRequest.getFoodId());
        var cartOptional = cartRepository.findById(cartItemRequest.getCartId());
        var cartItemOptional = cartItemRepository.findById(id);
        CartItem cartItem = cartItemOptional.get();
        if(cartItem.getFood().getId() == cartItemRequest.getFoodId() && cartItem.getCart().getId() == cartItemRequest.getCartId()) {
            cartItem.setQuantity(cartItemRequest.getQuantity());
        }
        else {
            cartItem.setFood(foodOptional.orElse(null));
            cartItem.setCart(cartOptional.orElse(null));
            cartItem.setQuantity(cartItemRequest.getQuantity());
        }
        cartItemMapper.updateCartItem(cartItem, cartItemRequest);
        cartItemRepository.save(cartItem);
        return mapCartItem(cartItem);
    }

    @Transactional
    public void delete(int id) {
        cartItemRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        cartItemRepository.deleteAll();
    }

    private CartItemResponse mapCartItem(CartItem cartItem) {
        var cartItemResponse = cartItemMapper.toCartItemResponse(cartItem);
        cartItemResponse.setFoodResponse(foodMapper.toFoodResponse(cartItem.getFood()));
//        cartItemResponse.setQuantity();
        return cartItemResponse;
    }
}
