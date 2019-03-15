package com.flywithus.service.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class PaymentOrder {
    
    @NonNull
    private Long purchasedObjectId;
    @NonNull
    private Long amountPlnToBePaid;
}
