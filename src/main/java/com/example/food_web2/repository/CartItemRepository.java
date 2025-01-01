package com.example.food_web2.repository;

import com.example.food_web2.entity.Cart;
import com.example.food_web2.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCartId(int id);
}