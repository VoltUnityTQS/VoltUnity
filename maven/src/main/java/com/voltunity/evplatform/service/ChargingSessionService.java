package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.ChargingSession;
import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.ChargingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ChargingSessionService {

    private static final Logger logger = LoggerFactory.getLogger(ChargingSessionService.class);

    @Autowired
    private ChargingSessionRepository chargingSessionRepository;

    @Autowired
    private SlotService slotService;

    @Autowired
    private UserService userService;

    public List<ChargingSession> getAllChargingSessions() {
        return chargingSessionRepository.findAll();
    }

    public ChargingSession getChargingSessionById(Long id) {
        return chargingSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Charging session not found with id: " + id));
    }

    public ChargingSession startSession(Long slotId, Long userId) {
        Slot slot = slotService.getSlotById(slotId);
        User user = userService.getUserById(userId);

        if (!slot.getSlotStatus().equalsIgnoreCase("AVAILABLE")) {
            throw new RuntimeException("Slot não disponível");
        }

        slot.setSlotStatus("IN_USE");
        slotService.updateSlot(slot);

        ChargingSession session = new ChargingSession();
        session.setSlot(slot);
        session.setUser(user);
        session.setSessionStatus("ACTIVE");
        session.setStartTimestamp(java.time.LocalDateTime.now());
        session.setEnergyConsumedKWh(0.0);

        ChargingSession saved = chargingSessionRepository.save(session);

        logger.info("Charging session iniciada: sessionId={}, slotId={}, userId={}", saved.getId(), slotId, userId);

        return saved;
    }

    public ChargingSession endSession(Long sessionId, double energyConsumed) {
        ChargingSession session = getChargingSessionById(sessionId);

        if (!session.getSessionStatus().equalsIgnoreCase("ACTIVE")) {
            throw new RuntimeException("Sessão não está ativa");
        }

        session.setEndTimestamp(java.time.LocalDateTime.now());
        session.setEnergyConsumedKWh(energyConsumed);
        session.setSessionStatus("COMPLETED");

        Slot slot = session.getSlot();
        slot.setSlotStatus("AVAILABLE");
        slotService.updateSlot(slot);

        ChargingSession saved = chargingSessionRepository.save(session);

        logger.info("Charging session FINALIZADA: sessionId={}, energyConsumed={} kWh", sessionId, energyConsumed);

        return saved;
    }

    public ChargingSession cancelSession(Long sessionId) {
        ChargingSession session = getChargingSessionById(sessionId);

        if (session.getSessionStatus().equalsIgnoreCase("COMPLETED") ||
            session.getSessionStatus().equalsIgnoreCase("CANCELLED")) {
            throw new RuntimeException("Sessão já está terminada ou cancelada");
        }

        session.setSessionStatus("CANCELLED");
        session.setEndTimestamp(java.time.LocalDateTime.now());

        Slot slot = session.getSlot();
        slot.setSlotStatus("AVAILABLE");
        slotService.updateSlot(slot);

        ChargingSession saved = chargingSessionRepository.save(session);

        logger.info("Charging session CANCELADA: sessionId={}", sessionId);

        return saved;
    }

}