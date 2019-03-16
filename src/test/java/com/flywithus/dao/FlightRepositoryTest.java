package com.flywithus.dao;

import com.flywithus.entity.Flight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FlightRepositoryTest {

    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void shouldFindFlights() {
        Set<Flight> flights = flightRepository.findFlightByArrivalLocationAndDepartureLocation("Warszawa", "Wroclaw");

        assertThat(flights.size()).isEqualTo(1);
        Flight flight = flights.iterator().next();
        assertThat(flight).extracting(Flight::getDepartureLocation).isEqualTo("Wroclaw");
        assertThat(flight).extracting(Flight::getArrivalLocation).isEqualTo("Warszawa");
        assertThat(flight).extracting(Flight::getAvailableDepartures).isNotNull();
    }

}
