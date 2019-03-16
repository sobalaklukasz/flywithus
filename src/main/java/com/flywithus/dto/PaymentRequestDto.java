package com.flywithus.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PaymentRequestDto {

    private long reservationId;
    private int amountPlnToBePaid;
    private long externalPaymentId;

}
