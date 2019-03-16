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
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Reservation reservation;

    @Column(nullable = false)
    private Long externalPaymentId;

    @Column(nullable = false)
    private boolean paid;

}
