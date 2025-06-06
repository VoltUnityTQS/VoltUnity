package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Booking;
import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChargingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SlotService slotService;

    @Mock
    private StationService stationService;

    @InjectMocks
    private ChargingService chargingService;

    private Station station;
    private Slot slot;
    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        station = new Station();
        station.setId(1L);

        slot = new Slot();
        slot.setId(10L);
        slot.setStation(station);
        slot.setSlotStatus("AVAILABLE");

        booking = new Booking();
        booking.setId(100L);
        booking.setSlot(slot);
    }

    @Test
    void testUnlockSlot_validBooking() {
        when(stationService.getStationById(1L)).thenReturn(station);
        when(slotService.getSlotById(10L)).thenReturn(slot);
        when(bookingRepository.findValidBookingForSlot(eq(slot), any(LocalDateTime.class))).thenReturn(Optional.of(booking));

        boolean result = chargingService.unlockSlot(100L, 1L, 10L);

        assertTrue(result);
        assertEquals("IN_USE", slot.getSlotStatus());
        verify(slotService).updateSlot(slot);
    }

    @Test
    void testUnlockSlot_invalidBooking_throws() {
        when(stationService.getStationById(1L)).thenReturn(station);
        when(slotService.getSlotById(10L)).thenReturn(slot);
        Booking otherBooking = new Booking();
        otherBooking.setId(200L);
        when(bookingRepository.findValidBookingForSlot(eq(slot), any(LocalDateTime.class))).thenReturn(Optional.of(otherBooking));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                chargingService.unlockSlot(100L, 1L, 10L)
        );
        assertEquals("Reserva inválida para este slot.", ex.getMessage());
        verify(slotService, never()).updateSlot(any());
    }

    @Test
    void testUnlockSlot_slotAvailable_noBooking() {
        when(stationService.getStationById(1L)).thenReturn(station);
        when(slotService.getSlotById(10L)).thenReturn(slot);
        when(bookingRepository.findValidBookingForSlot(eq(slot), any(LocalDateTime.class))).thenReturn(Optional.empty());

        boolean result = chargingService.unlockSlot(100L, 1L, 10L);

        assertTrue(result);
        assertEquals("IN_USE", slot.getSlotStatus());
        verify(slotService).updateSlot(slot);
    }

    @Test
    void testUnlockSlot_slotNotAvailable_noBooking_throws() {
        slot.setSlotStatus("IN_USE");

        when(stationService.getStationById(1L)).thenReturn(station);
        when(slotService.getSlotById(10L)).thenReturn(slot);
        when(bookingRepository.findValidBookingForSlot(eq(slot), any(LocalDateTime.class))).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                chargingService.unlockSlot(100L, 1L, 10L)
        );
        assertEquals("Slot não pode ser desbloqueado no momento.", ex.getMessage());
        verify(slotService, never()).updateSlot(any());
    }

    @Test
    void testUnlockSlot_slotDoesNotBelongToStation_throws() {
        Station anotherStation = new Station();
        anotherStation.setId(2L);

        when(stationService.getStationById(1L)).thenReturn(station);
        slot.setStation(anotherStation);
        when(slotService.getSlotById(10L)).thenReturn(slot);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                chargingService.unlockSlot(100L, 1L, 10L)
        );
        assertEquals("Slot não pertence à estação especificada.", ex.getMessage());
        verify(slotService, never()).updateSlot(any());
    }
}
