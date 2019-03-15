package com.flywithus.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flywithus.dao.FlightRepository;
import com.flywithus.dto.AvailableTicketDto;
import com.flywithus.entity.AvailableDepartureSeat;
import com.flywithus.entity.Flight;
import com.flywithus.entity.FlightDeparture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FlightController.class)
public class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightRepository flightRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldNotFindTicketBecauseOfLocation() throws Exception {
        Set<Flight> expectedFlights = prepareExpectedFlights();
        when(flightRepository.findFlightByArrivalLocationAndDepartureLocation("Wroclaw", "Warszawa")).thenReturn(expectedFlights);

        MvcResult mvcResult = this.mockMvc.perform(get("/flights?from=Warszawa&to=Wroclaw&date=20190101101010&amount=1").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        List<AvailableTicketDto> tickets = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<AvailableTicketDto>>() {
        });
        assertThat(tickets.size()).isEqualTo(0);
    }

    @Test
    public void shouldNotFindTicketsBecauseOfAmount() throws Exception {
        Set<Flight> expectedFlights = prepareExpectedFlights();
        when(flightRepository.findFlightByArrivalLocationAndDepartureLocation("Warszawa", "Wroclaw")).thenReturn(expectedFlights);

        MvcResult mvcResult = this.mockMvc.perform(get("/flights?from=Warszawa&to=Wroclaw&date=20190101101010&amount=3").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        List<AvailableTicketDto> tickets = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<AvailableTicketDto>>() {
        });
        assertThat(tickets.size()).isEqualTo(0);
    }

    @Test
    public void shouldFindAndReturnTickets() throws Exception {
        Set<Flight> expectedFlights = prepareExpectedFlights();
        when(flightRepository.findFlightByArrivalLocationAndDepartureLocation("Warszawa", "Wroclaw")).thenReturn(expectedFlights);
        ArgumentCaptor<String> departureCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> arrivalCaptor = ArgumentCaptor.forClass(String.class);

        MvcResult mvcResult = this.mockMvc.perform(get("/flights?from=Warszawa&to=Wroclaw&date=20190101101010&amount=1").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        List<AvailableTicketDto> tickets = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<AvailableTicketDto>>() {
        });
        assertThat(tickets.size()).isEqualTo(2);
        assertThat(tickets.get(0)).satisfies(ticket -> {
                    assertThat(ticket).extracting(AvailableTicketDto::getDepartureLocation).isEqualTo("Warszawa");
                    assertThat(ticket).extracting(AvailableTicketDto::getArrivalLocation).isEqualTo("Wroclaw");
                    assertThat(ticket).extracting(AvailableTicketDto::getDate).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2019, 1, 1, 10, 10, 10)));
                    assertThat(ticket).extracting(AvailableTicketDto::getRawPriceInPln).isEqualTo(10);
                }
        );
        assertThat(tickets.get(1)).satisfies(ticket -> {
                    assertThat(ticket).extracting(AvailableTicketDto::getDepartureLocation).isEqualTo("Warszawa");
                    assertThat(ticket).extracting(AvailableTicketDto::getArrivalLocation).isEqualTo("Wroclaw");
                    assertThat(ticket).extracting(AvailableTicketDto::getDate).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2019, 1, 1, 10, 10, 10)));
                    assertThat(ticket).extracting(AvailableTicketDto::getRawPriceInPln).isEqualTo(10);
                }
        );
        verify(flightRepository, times(1)).findFlightByArrivalLocationAndDepartureLocation(departureCaptor.capture(), arrivalCaptor.capture());
        assertThat(departureCaptor.getValue()).isEqualTo("Warszawa");
        assertThat(arrivalCaptor.getValue()).isEqualTo("Wroclaw");
    }

    private Set<Flight> prepareExpectedFlights() {
//        Flights
        Set<Flight> expectedFlights = new HashSet<>();
        Flight expectedFlight = new Flight();
        expectedFlight.setDepartureLocation("Warszawa");
        expectedFlight.setArrivalLocation("Wroclaw");
//        FlightDeparture
        Set<FlightDeparture> departures = new HashSet<>();
        FlightDeparture departure = new FlightDeparture();
        departure.setDepartureDate(Timestamp.valueOf(LocalDateTime.of(2019, 1, 1, 10, 10, 10)));
        departure.setFlight(expectedFlight);
//        AvailableDepartureSeat
        Set<AvailableDepartureSeat> seats = new HashSet<>();
        AvailableDepartureSeat seat1 = new AvailableDepartureSeat();
        seat1.setId(1L);
        seat1.setPriceInPln(10);
        seat1.setFlightDeparture(departure);
        seats.add(seat1);
        AvailableDepartureSeat seat2 = new AvailableDepartureSeat();
        seat2.setId(2L);
        seat2.setPriceInPln(10);
        seat2.setFlightDeparture(departure);
        seats.add(seat2);
        departure.setAvailableSeats(seats);

        departures.add(departure);
        expectedFlight.setAvailableDepartures(departures);
        expectedFlights.add(expectedFlight);
        return expectedFlights;
    }
}
