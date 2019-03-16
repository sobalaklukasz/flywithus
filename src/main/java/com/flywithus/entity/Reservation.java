package com.flywithus.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@EqualsAndHashCode
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservation")
    private List<AvailableDepartureSeat> availableDepartureSeat;

    @Column(nullable = false)
    private boolean paid;

    @Column
    private Date expiringDate;


}
