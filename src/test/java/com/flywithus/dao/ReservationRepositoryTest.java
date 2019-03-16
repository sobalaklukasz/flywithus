package com.flywithus.dao;

import com.flywithus.entity.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql("classpath:/initial-test-data.sql")
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void shouldFindReservationById() {
        Reservation reservation = reservationRepository.findReservationById(100001L);

        assertThat(reservation.getId()).isEqualTo(100001L);
        assertThat(reservation.getExpiringDate()).isInTheFuture();
        assertThat(reservation.getPayment().getId()).isEqualTo(10000001L);
    }

}
