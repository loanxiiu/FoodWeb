package com.example.food_web2.repository;

import com.example.food_web2.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery,Integer> {
    Optional<Delivery> findByOrderId(int orderId);
}
