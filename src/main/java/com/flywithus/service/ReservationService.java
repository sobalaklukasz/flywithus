package com.flywithus.service;

import com.flywithus.dao.AvailableDepartureSeatRepository;
import com.flywithus.dao.PaymentRepository;
import com.flywithus.dao.ReservationRepository;
import com.flywithus.entity.AvailableDepartureSeat;
import com.flywithus.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Set;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final AvailableDepartureSeatRepository availableDepartureSeatRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, PaymentRepository paymentRepository, AvailableDepartureSeatRepository availableDepartureSeatRepository) {
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
        this.availableDepartureSeatRepository = availableDepartureSeatRepository;
    }

    public Reservation reserveSeats(Set<Long> ticketIdsToReserve) {
        Reservation reservation = new Reservation();
        reservation.setAvailableDepartureSeat(availableDepartureSeatRepository.findAll(ticketIdsToReserve));
        reservation.setExpiringDate(Date.from(LocalDateTime.now().plusDays(2).toInstant(ZoneOffset.UTC)));
        return reservationRepository.save(reservation);
    }

    @Transactional
    public synchronized boolean cancelReservationOnDemand(long id) {
        if (!availableDepartureSeatRepository.findAvailableDepartureSeatsByReservationId(id).stream()
                .allMatch(this::canReservationBeCancelled)) {
            return false;
        }
        availableDepartureSeatRepository.cancelReservationForAvailableDepartureSeatsByReservationId(id);
        Reservation reservation = reservationRepository.findReservationById(id);
        paymentRepository.delete(reservation.getPayment().getId());
        return true;
    }

    @Transactional
    public synchronized void cancelReservationOnExpired(long id) {
        availableDepartureSeatRepository.cancelReservationForAvailableDepartureSeatsByReservationId(id);
        Reservation reservation = reservationRepository.findReservationById(id);
        paymentRepository.delete(reservation.getPayment().getId());
    }

    private boolean canReservationBeCancelled(AvailableDepartureSeat seat) {
        LocalDateTime fiveDaysTillDeparture = LocalDateTime.now().plusDays(5);
        return fiveDaysTillDeparture.isBefore(LocalDateTime.ofInstant(seat.getFlightDeparture().getDepartureDate().toInstant(), ZoneId.systemDefault()));
    }
}
