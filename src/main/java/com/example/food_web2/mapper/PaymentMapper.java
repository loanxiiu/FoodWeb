package com.example.food_web2.mapper;

import com.example.food_web2.dto.request.PaymentRequest;
import com.example.food_web2.dto.response.PaymentResponse;
import com.example.food_web2.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "order", ignore = true)
//    @Mapping(target = "amount", ignore = true)
    Payment toPayment(PaymentRequest paymentRequest);
    PaymentResponse toPaymentResponse(Payment payment);

    @Mapping(target = "order", ignore = true)
    void updatePayment(@MappingTarget Payment payment, PaymentRequest paymentRequest);
}
