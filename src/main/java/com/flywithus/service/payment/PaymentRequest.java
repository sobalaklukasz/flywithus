package com.flywithus.service.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class PaymentRequest {

    @NonNull
    private Boolean isPaid;

}
