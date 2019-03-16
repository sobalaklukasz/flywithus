package com.flywithus.controller;

import com.flywithus.dao.FlightRepository;
import com.flywithus.dto.AvailableTicketDto;
import com.flywithus.entity.Flight;
import com.flywithus.entity.FlightDeparture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @GetMapping
    public ResponseEntity searchForFlight(@RequestParam String from, @RequestParam String to, @RequestParam @DateTimeFormat(pattern = "yyyyMMddHHmmss") Date date, @RequestParam int amount) {

        Set<Flight> flights = flightRepository.findFlightByArrivalLocationAndDepartureLocation(from, to);

        List<AvailableTicketDto> tickets = flights.stream()
                .map(Flight::getAvailableDepartures)
                .flatMap(flightDepartures -> flightDepartures.stream().map(FlightDeparture::getAvailableSeats))
                .filter(seats -> seats.stream().filter(candidate -> candidate.getReservation() == null).count() >= amount)
                .flatMap(Collection::stream)
                .filter(seat -> seat.getFlightDeparture().getDepartureDate().getTime() == date.getTime())
                .map(seat -> AvailableTicketDto.builder()
                        .id(seat.getId())
                        .rawPriceInPln(seat.getPriceInPln())
                        .date(seat.getFlightDeparture().getDepartureDate())
                        .arrivalLocation(seat.getFlightDeparture().getFlight().getArrivalLocation())
                        .departureLocation(seat.getFlightDeparture().getFlight().getDepartureLocation())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(tickets);
    }

}
