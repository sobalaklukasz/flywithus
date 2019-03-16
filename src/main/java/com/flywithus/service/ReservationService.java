package com.flywithus.service;

import com.flywithus.dao.AvailableDepartureSeatRepository;
import com.flywithus.dao.ReservationRepository;
import com.flywithus.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Set;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AvailableDepartureSeatRepository availableDepartureSeatRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, AvailableDepartureSeatRepository availableDepartureSeatRepository) {
        this.reservationRepository = reservationRepository;
        this.availableDepartureSeatRepository = availableDepartureSeatRepository;
    }

    public Reservation reserveSeats(Set<Long> ticketIdsToReserve) {
        Reservation reservation = new Reservation();
        reservation.setAvailableDepartureSeat(availableDepartureSeatRepository.findAll(ticketIdsToReserve));
        reservation.setExpiringDate(Date.from(LocalDateTime.now().plusDays(2).toInstant(ZoneOffset.UTC)));
        return reservationRepository.save(reservation);
    }

    public synchronized void cancelReservation(long id) {
        availableDepartureSeatRepository.cancelReservationForAvailableDepartureSeatsByReservationId(id);
        reservationRepository.delete(id);
    }
}
