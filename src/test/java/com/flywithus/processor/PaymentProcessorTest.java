package com.flywithus.processor;

import com.flywithus.api.payment.DummyExternalPaymentGateway;
import com.flywithus.dao.PaymentRepository;
import com.flywithus.dto.PaymentRequestDto;
import com.flywithus.entity.Payment;
import com.flywithus.entity.Reservation;
import com.flywithus.service.ReservationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentProcessorTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private DummyExternalPaymentGateway dummyExternalPaymentGateway;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private PaymentProcessor paymentProcessor;

    @Test
    public void shouldCreatePaymentRequest() {
        Reservation reservation = new Reservation();
        reservation.setId(100L);
        reservation.setExpiringDate(Date.valueOf(LocalDate.MIN));
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);

        PaymentRequestDto paymentRequestDto = paymentProcessor.registerPaymentRequest(10, reservation);

        assertThat(paymentRequestDto.getReservationId()).isEqualTo(100L);
        assertThat(paymentRequestDto.getAmountPlnToBePaid()).isEqualTo(10);
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());
        Payment actualPayment = paymentCaptor.getValue();
        assertThat(actualPayment.getReservation()).isEqualTo(reservation);
    }

    @Test
    public void shouldCorrectlyCheckPayment() {
        Payment payment1 = new Payment();
        payment1.setId(1L);
        payment1.setExternalPaymentId(10L);
        payment1.setExternalPaymentId(11L);
        Reservation reservation1 = new Reservation();
        reservation1.setId(100L);
        reservation1.setExpiringDate(Date.valueOf(LocalDate.MAX));
        payment1.setReservation(reservation1);

        Payment payment2 = new Payment();
        payment2.setId(2L);
        payment2.setExternalPaymentId(20L);
        Reservation reservation2 = new Reservation();
        reservation2.setExpiringDate(Date.valueOf(LocalDate.MIN));
        reservation2.setId(200L);
        payment2.setReservation(reservation2);

        List<Payment> payments = Arrays.asList(payment1, payment2);
        when(paymentRepository.findAllByPaidIsFalse()).thenReturn(payments);
        when(dummyExternalPaymentGateway.isPaymentComplete(11L)).thenReturn(true);

        paymentProcessor.updatePaymentsToMonitor();
        paymentProcessor.checkPayment();

        verify(paymentRepository, times(1)).setPaymentAsDone(1L);
        verify(reservationService, times(1)).cancelReservationOnExpired(200L);
    }
}
