package com.flywithus.service;

import com.flywithus.dao.AvailableDepartureSeatRepository;
import com.flywithus.dto.ReservationDto;
import com.flywithus.entity.AvailableDepartureSeat;
import com.flywithus.entity.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationValidatorTest {

    @Mock
    private AvailableDepartureSeatRepository availableDepartureSeatRepository;

    @InjectMocks
    private ReservationValidator reservationValidator;

    @Test
    public void shouldDetectNotValid() {
        AvailableDepartureSeat reservedSeat = new AvailableDepartureSeat();
        reservedSeat.setId(1L);
        reservedSeat.setReservation(new Reservation());
        when(availableDepartureSeatRepository.findAll(anyCollection())).thenReturn(Arrays.asList(reservedSeat));

        boolean valid = reservationValidator.isValid(ReservationDto.builder().ticketIdsToReserve(new HashSet<>(Arrays.asList(1L))).build());

        assertThat(valid).isFalse();
    }

    @Test
    public void shouldDetectValid() {
        AvailableDepartureSeat notReservedSeat = new AvailableDepartureSeat();
        notReservedSeat.setId(1L);
        when(availableDepartureSeatRepository.findAll(anyCollection())).thenReturn(Arrays.asList(notReservedSeat));

        boolean valid = reservationValidator.isValid(ReservationDto.builder().ticketIdsToReserve(new HashSet<>(Arrays.asList(1L))).build());

        assertThat(valid).isTrue();
    }

}