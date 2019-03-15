package com.flywithus.dao;

import com.flywithus.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Set<Flight> findFlightByArrivalLocationAndDepartureLocation(String arrivalLocation, String departureLocation);
}
