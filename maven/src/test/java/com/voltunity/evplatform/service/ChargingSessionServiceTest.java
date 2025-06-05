package com.voltunity.evplatform.service;

import com.voltunity.evplatform.repository.ChargingSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class ChargingSessionServiceTest {

    @Mock
    private ChargingSessionRepository chargingSessionRepository;

    @InjectMocks
    private ChargingSessionService chargingSessionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetChargingSessionsByUser_shouldCallRepository() {
        Long userId = 1L;

        chargingSessionService.getChargingSessionsByUser(userId);

        verify(chargingSessionRepository).findByUser_IdAndStartTimestampBetween(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class));
    }
}