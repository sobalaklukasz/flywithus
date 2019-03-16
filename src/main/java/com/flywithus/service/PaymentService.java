package com.flywithus.service;

import com.flywithus.api.payment.PaymentGateway;
import com.flywithus.api.payment.PaymentOrder;
import com.flywithus.api.payment.PaymentRequest;
import com.flywithus.entity.Reservation;
import org.springframework.stereotype.Service;

@Service
// TODO: test
public class PaymentService {

    private final PriceCalculator priceCalculator;
    private final PaymentGateway paymentGateway;

    public PaymentService(PriceCalculator priceCalculator, PaymentGateway paymentGateway) {
        this.priceCalculator = priceCalculator;
        this.paymentGateway = paymentGateway;
    }

    public PaymentRequest createPaymentRequest(Reservation reservation, boolean isRegisterUser, boolean isFastCheckIn) {
        int price = priceCalculator.calculatePriceInPln(isRegisterUser, isFastCheckIn, reservation.getAvailableDepartureSeat());
        PaymentOrder paymentOrder = PaymentOrder.builder()
                .amountPlnToBePaid(price)
                .reservationId(reservation.getId())
                .build();

        return paymentGateway.createPaymentRequest(paymentOrder);
    }
}
