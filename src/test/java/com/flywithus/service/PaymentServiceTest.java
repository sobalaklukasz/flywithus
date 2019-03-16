package com.flywithus.service;

import com.flywithus.dto.PaymentRequestDto;
import com.flywithus.entity.AvailableDepartureSeat;
import com.flywithus.entity.Reservation;
import com.flywithus.processor.PaymentProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    @Mock
    private PriceCalculator priceCalculator;

    @Mock
    private PaymentProcessor paymentProcessor;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    public void shouldCreatePaymentRequest() {
        List<AvailableDepartureSeat> seats = Collections.emptyList();
        Reservation reservation = mock(Reservation.class);
        when(reservation.getAvailableDepartureSeat()).thenReturn(seats);
        when(priceCalculator.calculatePriceInPln(false, false, seats)).thenReturn(100);
        when(paymentProcessor.registerPaymentRequest(100, reservation)).thenReturn(PaymentRequestDto.builder().build());

        paymentService.createPaymentRequest(reservation, false, false);

        verify(paymentProcessor, times(1)).registerPaymentRequest(100, reservation);
    }

}