package com.example.food_web2.controller;

import com.example.food_web2.dto.request.DeliveryRequest;
import com.example.food_web2.dto.response.DeliveryResponse;
import com.example.food_web2.service.DeliveryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryController {
    DeliveryService deliveryService;

    @GetMapping
    List<DeliveryResponse> getAll(){
        return deliveryService.getAll();
    }

    @GetMapping("/{id}")
    DeliveryResponse getById(@PathVariable int id){
        return deliveryService.findById(id);
    }

    @GetMapping("/order/{id}")
    DeliveryResponse findByOrderId(@PathVariable int id){
        return deliveryService.findByOrderId(id);
    }

    @PostMapping
    DeliveryResponse addDelivery(@RequestBody DeliveryRequest deliveryRequest){
        return deliveryService.create(deliveryRequest);
    }

    @PutMapping("/{id}")
    DeliveryResponse updateDelivery(@PathVariable int id, @RequestBody DeliveryRequest deliveryRequest){
        return deliveryService.update(id, deliveryRequest);
    }

    @DeleteMapping
    void deleteDelivery(@PathVariable int id){
        deliveryService.delete(id);
    }
}
