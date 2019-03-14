package com.flywithus.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Setter
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Basic(optional = false)
    private String password;

    @Basic(optional = false)
    private String email;

}
