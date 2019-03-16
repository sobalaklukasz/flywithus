package com.flywithus.dao;

import com.flywithus.entity.Payment;
import com.flywithus.entity.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql("classpath:/initial-test-data.sql")
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void shouldFindOnlyIfPaidColumnIsFalse() {
        List<Payment> payments = paymentRepository.findAllByPaidIsFalse();

        payments.forEach(payment -> assertThat(payment.isPaid()).isFalse());
    }

    @Test
    public void shouldUpdatePaidColumn() {
        paymentRepository.setPaymentAsDone(10000001L);

        Payment paymentAfter = paymentRepository.findOne(10000001L);

        assertThat(paymentAfter.isPaid()).isTrue();
    }
}
