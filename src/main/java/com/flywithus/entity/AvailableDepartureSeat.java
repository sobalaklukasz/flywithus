package com.flywithus.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private FlightDeparture flightDeparture;

    @Column(nullable = false)
    private boolean reserved;
}
