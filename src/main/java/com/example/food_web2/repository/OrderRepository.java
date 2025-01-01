package com.example.food_web2.repository;

import com.example.food_web2.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCustomerId(int customerId);
    List<Order> findByOrderDate(Date orderDate);
    @Query("SELECT o FROM Order o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month")
    List<Order> findAllByYearAndMonth(@Param("year") int year, @Param("month") int month);
//    @Query("SELECT o FROM Order o WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month")
//    List<Order> findAllByYearAndMonth(@Param("year") int year, @Param("month") int month);
}
