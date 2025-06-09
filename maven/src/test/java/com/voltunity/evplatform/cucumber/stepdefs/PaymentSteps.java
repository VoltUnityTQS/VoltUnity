package com.voltunity.evplatform.cucumber.stepdefs;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.voltunity.evplatform.cucumber.TestContext;
import com.voltunity.evplatform.model.Booking;
import com.voltunity.evplatform.service.BookingService;
import com.voltunity.evplatform.service.PaymentService;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class PaymentSteps {

    @Autowired private BookingService bookingService;
    @Autowired private PaymentService paymentService;
    @Autowired private TestContext testContext;

    @When("the user pays {string} EUR")
    public void theUserPaysEUR(String amountStr) {
        testContext.paymentAmount = Double.parseDouble(amountStr);
        paymentService.createPayment(testContext.userId, testContext.paymentAmount, "EUR");
    }

    @When("the user books a slot from {string} to {string}")
    public void theUserBooksASlot(String from, String to) {
        Booking booking = bookingService.createBooking(
            testContext.stationId,
            LocalDateTime.parse(from),
            LocalDateTime.parse(to),
            testContext.userId
        );
        booking.setPriceAtBooking((float) testContext.paymentAmount);
        testContext.booking = bookingService.saveBooking(booking);
    }

    @Then("the booking price should be {double}")
    public void theBookingPriceShouldBe(Double expected) {
        assertNotNull(testContext.booking);
        assertEquals(expected, testContext.booking.getPriceAtBooking(), 0.001);
    }
}