package com.example.food_web2.repository;

import com.example.food_web2.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Integer> {
    boolean existsByName(String name);
    Optional<Food> findByName(String name);
    List<Food> findByNameContaining(String name);
    List<Food> findByBrandName(String brandName);
    Optional<Food> findById(int id);
}
