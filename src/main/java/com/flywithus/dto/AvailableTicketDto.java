package com.flywithus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTicketDto {

    private String departureLocation;
    private String arrivalLocation;
    private Date date;
    private int rawPriceInPln;

}
