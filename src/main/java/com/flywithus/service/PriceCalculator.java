package com.flywithus.service;

import com.flywithus.entity.AvailableDepartureSeat;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Stream;

@Component
public class PriceCalculator {

    private static final int FAST_CHECK_IN_PRICE_IN_PLN = 50;
    private static final float REGISTERED_USER_PRICE_AFTER_DISCOUNT_IN_PERCENTAGE = 0.95f;

    public int calculatePriceInPln(boolean isRegisterUser, boolean isFastCheckIn, Collection<AvailableDepartureSeat> seats) {
        if (seats == null || seats.size() < 1) throw new IllegalArgumentException("Atleast one seat must be present");

        Integer calculatedPrice = Stream.of(includePriceOfAllTickets(seats))
                .map(price -> includeFastCheckIn(isFastCheckIn, price))
                .map(price -> includeRegisterUserDiscount(isRegisterUser, price))
                .findAny()
                .get();
        return calculatedPrice < 0 ? 0 : calculatedPrice;
    }

    private int includePriceOfAllTickets(Collection<AvailableDepartureSeat> seats) {
        return seats.stream().mapToInt(AvailableDepartureSeat::getPriceInPln).sum();
    }

    private int includeRegisterUserDiscount(boolean isRegisterUser, int rawPriceInPln) {
        return isRegisterUser ? (int) (rawPriceInPln * REGISTERED_USER_PRICE_AFTER_DISCOUNT_IN_PERCENTAGE) : rawPriceInPln;
    }

    private int includeFastCheckIn(boolean isFastCheckIn, int rawPriceInPln) {
        return isFastCheckIn ? rawPriceInPln - FAST_CHECK_IN_PRICE_IN_PLN : rawPriceInPln;
    }
}
