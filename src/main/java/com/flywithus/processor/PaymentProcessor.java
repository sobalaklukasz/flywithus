package com.flywithus.processor;

import com.flywithus.api.payment.DummyExternalPaymentGateway;
import com.flywithus.dao.PaymentRepository;
import com.flywithus.dto.PaymentRequestDto;
import com.flywithus.entity.Payment;
import com.flywithus.entity.Reservation;
import com.flywithus.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
public class PaymentProcessor {

    private final PaymentRepository paymentRepository;
    private final DummyExternalPaymentGateway dummyExternalPaymentGateway;
    private final ReservationService reservationService;

    private List<Payment> paymentsToMonitor;

    @Autowired
    public PaymentProcessor(PaymentRepository paymentRepository, DummyExternalPaymentGateway dummyExternalPaymentGateway, ReservationService reservationService) {
        this.paymentRepository = paymentRepository;
        this.dummyExternalPaymentGateway = dummyExternalPaymentGateway;
        this.reservationService = reservationService;
    }

    @PostConstruct
    private void initPaymentsToMonitor() {
        updatePaymentsToMonitor();
    }


    public PaymentRequestDto registerPaymentRequest(int priceInPln, Reservation reservation) {

        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setExternalPaymentId(56478395748L);
        addNewPayment(payment);

        return PaymentRequestDto.builder()
                .amountPlnToBePaid(priceInPln)
                .reservationId(reservation.getId())
                .externalPaymentId(56478395748L) //random id used in external payment gateway
                .build();
    }

    private void addNewPayment(Payment payment) {
        paymentRepository.save(payment);
        updatePaymentsToMonitor();
    }

    protected void updatePaymentsToMonitor() {
        paymentsToMonitor = paymentRepository.findAllByPaidIsFalse();
    }

    @Scheduled(fixedRate = 10000)
    protected void checkPayment() {
        for (Payment payment : paymentsToMonitor) {
            if (dummyExternalPaymentGateway.isPaymentComplete(payment.getExternalPaymentId())) {
                paymentRepository.setPaymentAsDone(payment.getId());
            } else if (checkIfPaymentExpired(payment.getReservation())) {
                reservationService.cancelReservationOnExpired(payment.getReservation().getId());
            }
        }
        updatePaymentsToMonitor();
    }

    private boolean checkIfPaymentExpired(Reservation reservation) {
        return new Date().compareTo(reservation.getExpiringDate()) < 0;
    }
}
