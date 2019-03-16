package com.flywithus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flywithus.dto.PaymentRequestDto;
import com.flywithus.dto.ReservationDto;
import com.flywithus.entity.Reservation;
import com.flywithus.service.PaymentService;
import com.flywithus.service.ReservationService;
import com.flywithus.service.ReservationValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private ReservationValidator reservationValidator;

    @Test
    public void shouldReserve() throws Exception {
        Reservation reservation = new Reservation();
        PaymentRequestDto expectedPaymentRequestDto = PaymentRequestDto.builder().build();
        ReservationDto validReservation = ReservationDto.builder()
                .isFastCheckIn(false)
                .isRegisterUser(false)
                .ticketIdsToReserve(new HashSet<>(Arrays.asList(10002L)))
                .build();
        when(reservationValidator.isValid(validReservation)).thenReturn(true);
        when(reservationService.reserveSeats(validReservation.getTicketIdsToReserve())).thenReturn(reservation);
        when(paymentService.createPaymentRequest(reservation, validReservation.isRegisterUser(), validReservation.isFastCheckIn())).thenReturn(expectedPaymentRequestDto);

        MvcResult mvcResult = this.mockMvc.perform(post("/reservations").content(mapper.writeValueAsString(validReservation)).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(mapper.readValue(mvcResult.getResponse().getContentAsString(), PaymentRequestDto.class)).isEqualTo(expectedPaymentRequestDto);
    }

    @Test
    public void shouldReturnBadRequestDueToValidation() throws Exception {
        ReservationDto notValidReservation = ReservationDto.builder()
                .isFastCheckIn(false)
                .isRegisterUser(false)
                .ticketIdsToReserve(Collections.emptySet())
                .build();
        this.mockMvc.perform(post("/reservations").content(mapper.writeValueAsString(notValidReservation)).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Reservation candidate is incorrect.")));
    }

    @Test
    public void shouldReturnBadRequestDueToAlreadyReservedSeat() throws Exception {
        ReservationDto notValidReservation = ReservationDto.builder()
                .isFastCheckIn(false)
                .isRegisterUser(false)
                .ticketIdsToReserve(new HashSet<>(Arrays.asList(10002L)))
                .build();
        when(reservationValidator.isValid(notValidReservation)).thenReturn(false);
        this.mockMvc.perform(post("/reservations").content(mapper.writeValueAsString(notValidReservation)).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Cannot reserve tickets: [10002].")));
    }

    @Test
    public void shouldCancelReservation() throws Exception {
        this.mockMvc.perform(delete("/reservations").content("100001").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        verify(reservationService, times(1)).cancelReservationOnDemand(eq(100001L));
    }
}
