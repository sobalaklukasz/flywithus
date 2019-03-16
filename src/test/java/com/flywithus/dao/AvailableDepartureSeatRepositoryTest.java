package com.flywithus.dao;

import com.flywithus.entity.AvailableDepartureSeat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AvailableDepartureSeatRepositoryTest {

    @Autowired
    private AvailableDepartureSeatRepository availableDepartureSeatRepository;

    @Test
    public void shouldCancelReservation() {
        Set<AvailableDepartureSeat> seatsBefore = availableDepartureSeatRepository.findAvailableDepartureSeatsByReservationId(100001L);
        assertThat(seatsBefore.size()).isEqualTo(2);
        seatsBefore.forEach(seat -> assertThat(seat.getReservation().getId()).isEqualTo(100001L));

        availableDepartureSeatRepository.cancelReservationForAvailableDepartureSeatsByReservationId(100001L);

        Set<AvailableDepartureSeat> seatsAfter = availableDepartureSeatRepository.findAvailableDepartureSeatsByReservationId(100001L);
        assertThat(seatsAfter.size()).isEqualTo(0);
    }
}
