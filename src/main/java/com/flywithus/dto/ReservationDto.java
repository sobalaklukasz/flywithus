package com.flywithus.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReservationDto {

    private boolean isRegisterUser;

    @Size(min = 1)
    private Set<Long> ticketIdsToReserve;
    private boolean isFastCheckIn;
}
