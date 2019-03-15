package com.flywithus.service.payment;

/**
 * Very simple API which allows to create payment request which can be used pay for ordered item.
 */
public interface PaymentGateway {

    /**
     * This method is used to create payment request which can be used to track payment for order input.
     *
     * @param paymentOrder contains detail used to create PaymentRequest
     * @return PaymentRequest which can be used to pay for order.
     */
    PaymentRequest createPaymentRequest(PaymentOrder paymentOrder);

    /**
     * Updates data of payment request, can be used to track if request is paid.
     *
     * @param paymentRequest to be updated
     */
    void updatePaymentRequest(PaymentRequest paymentRequest);
}
