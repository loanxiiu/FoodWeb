package com.example.food_web2.repository;

import com.example.food_web2.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findById(int id);

//    @Query("SELECT c FROM Customer c WHERE c.user.id = :userId")
//    Customer findByUserId(@Param("userId") int userId);
    Customer findByUserId(int userId);
}
