package com.example.food_web2.repository;

import com.example.food_web2.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByOrderId(int orderId);
}
