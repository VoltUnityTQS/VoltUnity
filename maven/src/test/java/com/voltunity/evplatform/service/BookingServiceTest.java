package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Booking;
import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.BookingRepository;
import com.voltunity.evplatform.repository.SlotRepository;
import com.voltunity.evplatform.repository.StationRepository;
import com.voltunity.evplatform.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SlotService slotService;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SlotRepository slotRepository;

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

    @Test
    void testCancelBooking_bookingNotFound_throwsException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.cancelBooking(1L));
        assertEquals("Reserva não encontrada", ex.getMessage());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testCancelBooking_slotAlreadyAvailable() {
        Slot slot = new Slot();
        slot.setSlotStatus("AVAILABLE");

        Booking booking = new Booking();
        booking.setId(2L);
        booking.setSlot(slot);
        booking.setBookingStatus("confirmed");

        when(bookingRepository.findById(2L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.cancelBooking(2L);

        assertEquals("cancelled", result.getBookingStatus());
        // Slot status remains AVAILABLE
        assertEquals("AVAILABLE", slot.getSlotStatus());
        verify(bookingRepository).save(result);
    }

    @Test
    void testCancelBooking_shouldNotChangeOtherBookingProperties() {
        Slot slot = new Slot();
        slot.setSlotStatus("IN_USE");

        User user = new User();
        user.setId(5L);

        Booking booking = new Booking();
        booking.setId(5L);
        booking.setSlot(slot);
        booking.setUser(user);
        booking.setBookingStatus("confirmed");

        when(bookingRepository.findById(5L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.cancelBooking(5L);

        assertEquals("cancelled", result.getBookingStatus());
        assertEquals("AVAILABLE", slot.getSlotStatus());
        assertEquals(user, result.getUser());
        verify(bookingRepository).save(result);
    }

    @Test
    void testCreateBooking_successfulBooking() {
        Long stationId = 1L;
        Long userId = 2L;
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        Station station = new Station();
        User user = new User();

        Slot slot = new Slot();
        slot.setSlotStatus("AVAILABLE");

        when(stationRepository.findById(stationId)).thenReturn(Optional.of(station));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(slotRepository.findByStation(station)).thenReturn(List.of(slot));
        when(bookingRepository.findBySlotAndTimeOverlap(slot, start, end)).thenReturn(Collections.emptyList());
        when(slotRepository.save(slot)).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking booking = bookingService.createBooking(stationId, start, end, userId);

        assertNotNull(booking);
        assertEquals(user, booking.getUser());
        assertEquals(slot, booking.getSlot());
        assertEquals("confirmed", booking.getBookingStatus());
        assertEquals("IN_USE", slot.getSlotStatus());

        verify(slotRepository).save(slot);
        verify(bookingRepository).save(booking);
    }

    @Test
    void testCreateBooking_invalidInterval_throwsException() {
        Long stationId = 1L;
        Long userId = 2L;
        LocalDateTime now = LocalDateTime.now();

        // start after end
        LocalDateTime start = now.plusHours(3);
        LocalDateTime end = now.plusHours(2);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(stationId, start, end, userId));
        assertEquals("Intervalo de tempo inválido", ex.getMessage());

        // start before now
        LocalDateTime start2 = now.minusHours(1);
        LocalDateTime end2 = now.plusHours(1);

        ex = assertThrows(IllegalArgumentException.class,
                () -> bookingService.createBooking(stationId, start2, end2, userId));
        assertEquals("Intervalo de tempo inválido", ex.getMessage());
    }

    @Test
    void testCreateBooking_stationNotFound_throwsException() {
        when(stationRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), 1L));
        assertEquals("Estação não encontrada", ex.getMessage());
    }

    @Test
    void testCreateBooking_userNotFound_throwsException() {
        Station station = new Station();
        when(stationRepository.findById(1L)).thenReturn(Optional.of(station));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), 1L));
        assertEquals("Utilizador não encontrado", ex.getMessage());
    }

    @Test
    void testCreateBooking_noAvailableSlots_throwsException() {
        Station station = new Station();
        User user = new User();
        Slot slot = new Slot();
        slot.setSlotStatus("IN_USE");

        when(stationRepository.findById(1L)).thenReturn(Optional.of(station));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(slotRepository.findByStation(station)).thenReturn(List.of(slot));
        when(bookingRepository.findBySlotAndTimeOverlap(eq(slot), any(), any())).thenReturn(List.of(new Booking()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookingService.createBooking(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), 1L));
        assertEquals("Nenhum slot disponível na estação para o período selecionado", ex.getMessage());
    }

    @Test
    void testGetBooking_found() {
        Booking booking = new Booking();
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBooking(1L);

        assertEquals(booking, result);
    }

    @Test
    void testGetBooking_notFound_throwsException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.getBooking(1L));
        assertEquals("Reserva não encontrada", ex.getMessage());
    }

    @Test
    void testSaveBooking_shouldCallRepositorySave() {
        Booking booking = new Booking();
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking saved = bookingService.saveBooking(booking);

        assertEquals(booking, saved);
        verify(bookingRepository).save(booking);
    }

    @Test
    void testGetBookingsByUser_returnsList() {
        User user = new User();
        user.setId(1L);

        Booking booking1 = new Booking();
        booking1.setUser(user);

        Booking booking2 = new Booking();
        booking2.setUser(user);

        when(bookingRepository.findByUser_Id(1L)).thenReturn(List.of(booking1, booking2));

        List<Booking> bookings = bookingService.getBookingsByUser(1L);

        assertEquals(2, bookings.size());
        verify(bookingRepository).findByUser_Id(1L);
    }
}
