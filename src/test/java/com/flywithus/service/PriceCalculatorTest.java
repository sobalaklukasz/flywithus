package com.flywithus.service;

import com.flywithus.entity.AvailableDepartureSeat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class PriceCalculatorTest {

    private boolean isRegisterUser;
    private boolean isFastCheckIn;
    private Collection<AvailableDepartureSeat> seats;
    private int expected;

    private PriceCalculator priceCalculator = new PriceCalculator();

    public PriceCalculatorTest(boolean isRegisterUser, boolean isFastCheckIn, Collection<AvailableDepartureSeat> seats, int expected) {
        this.isRegisterUser = isRegisterUser;
        this.isFastCheckIn = isFastCheckIn;
        this.seats = seats;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        AvailableDepartureSeat availableDepartureSeatPrice10 = new AvailableDepartureSeat();
        availableDepartureSeatPrice10.setPriceInPln(10);

        AvailableDepartureSeat availableDepartureSeatPrice100 = new AvailableDepartureSeat();
        availableDepartureSeatPrice100.setPriceInPln(100);

        AvailableDepartureSeat availableDepartureSeatPrice150 = new AvailableDepartureSeat();
        availableDepartureSeatPrice150.setPriceInPln(150);

        return Arrays.asList(new Object[][]{
                {false, false, Arrays.asList(availableDepartureSeatPrice10), 10},
                {false, false, Arrays.asList(availableDepartureSeatPrice10, availableDepartureSeatPrice100), 110},
                {false, true, Arrays.asList(availableDepartureSeatPrice100), 50},
                {false, true, Arrays.asList(availableDepartureSeatPrice10), 0},
                {true, false, Arrays.asList(availableDepartureSeatPrice100), 95},
                {true, true, Arrays.asList(availableDepartureSeatPrice150), 95}
        });
    }

    @Test
    public void shouldCorrectlyCalculatePrice() {
        assertThat(priceCalculator.calculatePriceInPln(isRegisterUser, isFastCheckIn, seats)).isEqualTo(expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldValidateWrongNullArguments() {
        priceCalculator.calculatePriceInPln(false, false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldValidateWrongEmptySeatsArguments() {
        priceCalculator.calculatePriceInPln(false, false, Collections.emptySet());
    }
}
