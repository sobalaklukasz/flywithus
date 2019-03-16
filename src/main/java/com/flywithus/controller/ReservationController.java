package com.flywithus.controller;

import com.flywithus.dto.ReservationDto;
import com.flywithus.entity.Reservation;
import com.flywithus.service.PaymentService;
import com.flywithus.service.ReservationService;
import com.flywithus.service.ReservationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final ReservationValidator reservationValidator;

    @Autowired
    public ReservationController(ReservationService reservationService, PaymentService paymentService, ReservationValidator reservationValidator) {
        this.reservationService = reservationService;
        this.paymentService = paymentService;
        this.reservationValidator = reservationValidator;
    }

    @PostMapping
    public synchronized ResponseEntity createPaymentRequestForReservation(@Valid @RequestBody ReservationDto reservationDto, BindingResult result) {

        if (result.hasErrors()) return ResponseEntity.badRequest().body("Reservation candidate is incorrect.");

        if (!reservationValidator.isValid(reservationDto)) {
            return ResponseEntity.badRequest().body(String.format("Cannot reserve tickets: %s.", reservationDto.getTicketIdsToReserve()));
        }

        Reservation reservation = reservationService.reserveSeats(reservationDto.getTicketIdsToReserve());
        return ResponseEntity.ok().body(paymentService.createPaymentRequest(reservation, reservationDto.isRegisterUser(), reservationDto.isFastCheckIn()));
    }

    @DeleteMapping
    public ResponseEntity cancelReservation(@RequestBody long id) {
        reservationService.cancelReservationOnDemand(id);
        return ResponseEntity.ok().build();
    }
}
