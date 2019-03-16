package com.flywithus.service;

import com.flywithus.dao.AvailableDepartureSeatRepository;
import com.flywithus.dto.ReservationDto;
import com.flywithus.entity.AvailableDepartureSeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationValidator {

    private final AvailableDepartureSeatRepository availableDepartureSeatRepository;

    @Autowired
    public ReservationValidator(AvailableDepartureSeatRepository availableDepartureSeatRepository) {
        this.availableDepartureSeatRepository = availableDepartureSeatRepository;
    }

    public boolean isValid(ReservationDto reservationDto) {
        Set<Long> ticketIdsToReserve = reservationDto.getTicketIdsToReserve();
        Set<AvailableDepartureSeat> availableDepartureSeat = availableDepartureSeatRepository.findAll(ticketIdsToReserve).stream()
                .filter(seat -> seat.getReservation() == null)
                .collect(Collectors.toSet());

        return availableDepartureSeat.stream().map(AvailableDepartureSeat::getId).collect(Collectors.toSet()).containsAll(ticketIdsToReserve);
    }
}
