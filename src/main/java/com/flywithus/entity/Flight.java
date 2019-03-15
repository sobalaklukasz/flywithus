package com.flywithus.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
public class Flight {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String departureLocation;

    @Column(nullable = false)
    private String arrivalLocation;

    @Column(nullable = false)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "flight", orphanRemoval = true)
    private Set<FlightDeparture> availableDepartures;
}
