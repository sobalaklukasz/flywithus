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

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "reservation")
    private Payment payment;

    @Column
    private Date expiringDate;


}
