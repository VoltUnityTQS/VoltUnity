package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.ChargingSession;
import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.ChargingSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ChargingSessionServiceTest {

    @Mock
    private ChargingSessionRepository chargingSessionRepository;

    @Mock
    private SlotService slotService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ChargingSessionService chargingSessionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllChargingSessions_shouldCallRepository() {
        List<ChargingSession> sessions = List.of(new ChargingSession(), new ChargingSession());
        when(chargingSessionRepository.findAll()).thenReturn(sessions);

        List<ChargingSession> result = chargingSessionService.getAllChargingSessions();

        assertEquals(2, result.size());
        verify(chargingSessionRepository).findAll();
    }

    @Test
    void testGetChargingSessionById_found() {
        ChargingSession session = new ChargingSession();
        session.setId(1L);

        when(chargingSessionRepository.findById(1L)).thenReturn(Optional.of(session));

        ChargingSession result = chargingSessionService.getChargingSessionById(1L);

        assertEquals(1L, result.getId());
        verify(chargingSessionRepository).findById(1L);
    }

    @Test
    void testGetChargingSessionById_notFound_throwsException() {
        when(chargingSessionRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> chargingSessionService.getChargingSessionById(1L));
        assertEquals("Charging session not found with id: 1", ex.getMessage());
    }

    @Test
    void testStartSession_success() {
        Slot slot = new Slot();
        slot.setId(1L);
        slot.setSlotStatus("AVAILABLE");

        User user = new User();
        user.setId(2L);

        ChargingSession savedSession = new ChargingSession();
        savedSession.setId(3L);
        savedSession.setSlot(slot);
        savedSession.setUser(user);
        savedSession.setSessionStatus("ACTIVE");

        when(slotService.getSlotById(1L)).thenReturn(slot);
        when(userService.getUserById(2L)).thenReturn(user);
        when(chargingSessionRepository.save(any(ChargingSession.class))).thenReturn(savedSession);

        ChargingSession result = chargingSessionService.startSession(1L, 2L);

        assertEquals("ACTIVE", result.getSessionStatus());
        assertEquals(slot, result.getSlot());
        assertEquals(user, result.getUser());
        verify(slotService).updateSlot(slot);
    }

    @Test
    void testStartSession_slotNotAvailable_throwsException() {
        Slot slot = new Slot();
        slot.setSlotStatus("IN_USE");

        when(slotService.getSlotById(1L)).thenReturn(slot);
        when(userService.getUserById(2L)).thenReturn(new User());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> chargingSessionService.startSession(1L, 2L));
        assertEquals("Slot não disponível", ex.getMessage());
        verify(slotService, never()).updateSlot(any());
        verify(chargingSessionRepository, never()).save(any());
    }

    @Test
    void testEndSession_success() {
        Slot slot = new Slot();
        slot.setSlotStatus("IN_USE");

        ChargingSession session = new ChargingSession();
        session.setId(1L);
        session.setSessionStatus("ACTIVE");
        session.setSlot(slot);

        when(chargingSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(chargingSessionRepository.save(any(ChargingSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChargingSession result = chargingSessionService.endSession(1L, 10.5);

        assertEquals("COMPLETED", result.getSessionStatus());
        assertEquals(10.5, result.getEnergyConsumedKWh());
        assertNotNull(result.getEndTimestamp());
        assertEquals("AVAILABLE", slot.getSlotStatus());
        verify(slotService).updateSlot(slot);
    }

    @Test
    void testEndSession_notActive_throwsException() {
        ChargingSession session = new ChargingSession();
        session.setSessionStatus("COMPLETED");

        when(chargingSessionRepository.findById(1L)).thenReturn(Optional.of(session));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> chargingSessionService.endSession(1L, 10.0));
        assertEquals("Sessão não está ativa", ex.getMessage());
        verify(slotService, never()).updateSlot(any());
        verify(chargingSessionRepository, never()).save(any());
    }

    @Test
    void testCancelSession_success() {
        Slot slot = new Slot();
        slot.setSlotStatus("IN_USE");

        ChargingSession session = new ChargingSession();
        session.setSessionStatus("ACTIVE");
        session.setSlot(slot);

        when(chargingSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(chargingSessionRepository.save(any(ChargingSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChargingSession result = chargingSessionService.cancelSession(1L);

        assertEquals("CANCELLED", result.getSessionStatus());
        assertNotNull(result.getEndTimestamp());
        assertEquals("AVAILABLE", slot.getSlotStatus());
        verify(slotService).updateSlot(slot);
    }

    @Test
    void testCancelSession_alreadyCompletedOrCancelled_throwsException() {
        ChargingSession session = new ChargingSession();
        session.setSessionStatus("COMPLETED");

        when(chargingSessionRepository.findById(1L)).thenReturn(Optional.of(session));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> chargingSessionService.cancelSession(1L));
        assertEquals("Sessão já está terminada ou cancelada", ex.getMessage());
        verify(slotService, never()).updateSlot(any());
        verify(chargingSessionRepository, never()).save(any());
    }

    @Test
    void testGetChargingSessionsByUser_callsRepository() {
        Long userId = 99L;

        chargingSessionService.getChargingSessionsByUser(userId);

        verify(chargingSessionRepository).findByUser_IdAndStartTimestampBetween(
                eq(userId),
                any(LocalDateTime.class),
                any(LocalDateTime.class));
    }
}
