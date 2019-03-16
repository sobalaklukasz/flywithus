package com.flywithus.api.payment;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PaymentRequest {

    @NonNull
    private Boolean isPaid;

}
