package com.example.food_web2.service;

import com.example.food_web2.dto.request.DeliveryRequest;
import com.example.food_web2.dto.response.DeliveryResponse;
import com.example.food_web2.entity.Delivery;
import com.example.food_web2.mapper.DeliveryMapper;
import com.example.food_web2.repository.DeliveryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryService {
    DeliveryRepository deliveryRepository;
    DeliveryMapper deliveryMapper;

    public List<DeliveryResponse> getAll() {
        return deliveryRepository.findAll()
                .stream().map(deliveryMapper::toDeliveryResponse)
                .toList();
    }

    public DeliveryResponse findById(int id) {
        var deliveryOptional = deliveryRepository.findById(id);
        Delivery delivery = deliveryOptional.get();
        return deliveryMapper.toDeliveryResponse(delivery);
    }

    public DeliveryResponse findByOrderId(int orderId) {
        var deliveryOptional = deliveryRepository.findByOrderId(orderId);
        Delivery delivery = deliveryOptional.get();
        return deliveryMapper.toDeliveryResponse(delivery);
    }

    public DeliveryResponse create(DeliveryRequest deliveryRequest) {
        Delivery delivery = deliveryMapper.toDelivery(deliveryRequest);
        return deliveryMapper.toDeliveryResponse(deliveryRepository.save(delivery));
    }

    public DeliveryResponse update(int id, DeliveryRequest deliveryRequest) {
        var deliveryOptional = deliveryRepository.findById(id);
        Delivery delivery = deliveryOptional.get();
        deliveryMapper.updateDelivery(delivery, deliveryRequest);
        deliveryRepository.save(delivery);
        return deliveryMapper.toDeliveryResponse(delivery);
    }

    public void delete(int id) {
        deliveryRepository.findById(id);
    }
}
