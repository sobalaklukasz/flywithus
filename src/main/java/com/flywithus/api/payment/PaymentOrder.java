package com.flywithus.api.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class PaymentOrder {
    
    @NonNull
    private long reservationId;
    @NonNull
    private int amountPlnToBePaid;
}
