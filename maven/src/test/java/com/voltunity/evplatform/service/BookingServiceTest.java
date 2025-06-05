
package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Booking;
import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SlotService slotService;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCancelBooking_shouldCancelAndSetSlotAvailable() {
        Slot slot = new Slot();
        slot.setSlotStatus("IN_USE");

        User user = new User();
        user.setId(1L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setSlot(slot);
        booking.setUser(user);
        booking.setBookingStatus("confirmed");

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.cancelBooking(1L);

        assertEquals("cancelled", result.getBookingStatus());
        assertEquals("AVAILABLE", slot.getSlotStatus());
        verify(bookingRepository).save(result);
    }
}