package com.flywithus.api.payment;

import org.springframework.stereotype.Component;

@Component
public class DummyPaymentGateway implements PaymentGateway{

    @Override
    public PaymentRequest createPaymentRequest(PaymentOrder paymentOrder) {
        return null;
    }

    @Override
    public void updatePaymentRequest(PaymentRequest paymentRequest) {

    }
}
