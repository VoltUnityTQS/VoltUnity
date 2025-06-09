package com.voltunity.evplatform.cucumber.stepdefs;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.voltunity.evplatform.cucumber.TestContext;
import com.voltunity.evplatform.model.Booking;
import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.SlotRepository;
import com.voltunity.evplatform.repository.StationRepository;
import com.voltunity.evplatform.repository.UserRepository;
import com.voltunity.evplatform.service.BookingService;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class BookingSteps {

    @Autowired private StationRepository stationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SlotRepository slotRepository;
    @Autowired private BookingService bookingService;
    @Autowired private TestContext testContext;

    private Exception exception;

    @Given("a station with available slots exists")
    public void aStationWithAvailableSlotsExists() {
        Station station = new Station();
        station.setName("Booking Station");
        station.setStationStatus("ACTIVE");
        testContext.stationId = stationRepository.save(station).getId();

        Slot slot = new Slot();
        slot.setSlotStatus("AVAILABLE");
        slot.setStation(station);
        slotRepository.save(slot);
    }

    @Given("a station without available slots exists")
    public void aStationWithoutAvailableSlotsExists() {
        Station station = new Station();
        station.setName("Unavailable Station");
        station.setStationStatus("ACTIVE");
        testContext.stationId = stationRepository.save(station).getId();

        Slot slot = new Slot();
        slot.setSlotStatus("IN_USE");
        slot.setStation(station);
        slotRepository.save(slot);
    }

    @Given("a registered user exists")
    public void aRegisteredUserExists() {
        User user = new User();
        user.setEmail("user@example.com");
        testContext.userId = userRepository.save(user).getId();
    }

    @When("I book a slot from {string} to {string}")
    public void iBookASlotFromTo(String startStr, String endStr) {
        try {
            testContext.booking = bookingService.createBooking(
                testContext.stationId,
                LocalDateTime.parse(startStr),
                LocalDateTime.parse(endStr),
                testContext.userId
            );
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the booking should be confirmed")
    public void theBookingShouldBeConfirmed() {
        assertNotNull(testContext.booking);
        assertEquals("confirmed", testContext.booking.getBookingStatus());
    }

    @Then("an error should occur with message {string}")
    public void anErrorShouldOccurWithMessage(String expectedMessage) {
        assertNotNull(exception, "Expected an exception but none was thrown");
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Given("a confirmed booking exists")
    public void aConfirmedBookingExists() {
        Station station = new Station();
        station.setName("Cancel Station");
        station.setStationStatus("ACTIVE");
        station = stationRepository.save(station);

        Slot slot = new Slot();
        slot.setSlotStatus("AVAILABLE");
        slot.setStation(station);
        slot = slotRepository.save(slot);

        User user = new User();
        user.setEmail("cancel@example.com");
        user = userRepository.save(user);

        Booking b = new Booking();
        b.setSlot(slot);
        b.setUser(user);
        b.setStart(LocalDateTime.now().plusHours(1));
        b.setEnd_time(LocalDateTime.now().plusHours(2));
        b.setBookingStatus("confirmed");
        b.setPriceAtBooking(5.0f);
        testContext.booking = bookingService.saveBooking(b);

        testContext.stationId = station.getId();
        testContext.userId = user.getId();
    }

    @When("I cancel the booking")
    public void iCancelTheBooking() {
        testContext.booking = bookingService.cancelBooking(testContext.booking.getId());
    }

    @Then("the booking status should be {string}")
    public void theBookingStatusShouldBe(String expectedStatus) {
        assertEquals(expectedStatus, testContext.booking.getBookingStatus());
    }

    @Then("the associated slot should be {string}")
    public void theAssociatedSlotShouldBe(String expectedStatus) {
        Slot updatedSlot = testContext.booking.getSlot();
        assertEquals(expectedStatus, updatedSlot.getSlotStatus());
    }
}