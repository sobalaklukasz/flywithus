package com.flywithus.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class AvailableDepartureSeat {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int priceInPln;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flight_departure_id")
    @JsonIgnore
    private FlightDeparture flightDeparture;
}
