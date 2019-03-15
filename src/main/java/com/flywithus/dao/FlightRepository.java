package com.flywithus.dao;

import com.flywithus.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Flight findFlightByArrivalLocation(String arrivalLocation);

    Flight findFlightByDepartureLocation(String departureLocation);
}
