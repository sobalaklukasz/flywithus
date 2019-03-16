package com.flywithus.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@EqualsAndHashCode(exclude = "flightDeparture")
public class AvailableDepartureSeat {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int priceInPln;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flight_departure_id")
    private FlightDeparture flightDeparture;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
