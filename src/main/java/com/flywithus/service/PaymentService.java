package com.flywithus.service;

import com.flywithus.dto.PaymentRequestDto;
import com.flywithus.entity.Reservation;
import com.flywithus.processor.PaymentProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PriceCalculator priceCalculator;
    private final PaymentProcessor paymentProcessor;

    @Autowired
    public PaymentService(PriceCalculator priceCalculator, PaymentProcessor paymentProcessor) {
        this.priceCalculator = priceCalculator;
        this.paymentProcessor = paymentProcessor;
    }

    public PaymentRequestDto createPaymentRequest(Reservation reservation, boolean isRegisterUser, boolean isFastCheckIn) {
        int price = priceCalculator.calculatePriceInPln(isRegisterUser, isFastCheckIn, reservation.getAvailableDepartureSeat());
        return paymentProcessor.registerPaymentRequest(price, reservation);
    }
}
