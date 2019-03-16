package com.flywithus.api.payment;

import org.springframework.stereotype.Component;

@Component
public class DummyExternalPaymentGateway {

    public boolean isPaymentComplete(long paymentId) {
        return false;
    }
}
