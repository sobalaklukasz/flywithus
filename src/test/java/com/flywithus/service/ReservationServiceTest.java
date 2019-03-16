package com.flywithus.service;

import com.flywithus.dao.AvailableDepartureSeatRepository;
import com.flywithus.dao.ReservationRepository;
import com.flywithus.entity.AvailableDepartureSeat;
import com.flywithus.entity.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AvailableDepartureSeatRepository availableDepartureSeatRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void shouldReserve() {
        HashSet<Long> idsToReserve = new HashSet<>(Arrays.asList(1L));
        AvailableDepartureSeat notReservedSeat = new AvailableDepartureSeat();
        notReservedSeat.setId(1L);
        List<AvailableDepartureSeat> seats = Arrays.asList(notReservedSeat);
        when(availableDepartureSeatRepository.findAll(anyCollection())).thenReturn(seats);
        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);

        reservationService.reserveSeats(idsToReserve);

        verify(reservationRepository, times(1)).save(captor.capture());
        Reservation reservation = captor.getValue();
        assertThat(reservation.isPaid()).isFalse();
        assertThat(reservation.getAvailableDepartureSeat()).isEqualTo(seats);
        assertThat(reservation.getExpiringDate()).isAfter(new Date());
    }

    @Test
    public void shouldCancelReserve() {
        reservationService.cancelReservation(100001L);

        verify(availableDepartureSeatRepository, times(1)).cancelReservationForAvailableDepartureSeatsByReservationId(100001L);
        verify(reservationRepository, times(1)).delete(100001L);
    }
}