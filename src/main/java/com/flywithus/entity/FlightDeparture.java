package com.flywithus.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Entity
@EqualsAndHashCode(exclude = "flight")
public class FlightDeparture {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Date departureDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Column(nullable = false)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flightDeparture", orphanRemoval = true)
    private Set<AvailableDepartureSeat> availableSeats;

}
