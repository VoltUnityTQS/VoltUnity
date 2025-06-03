package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.ChargingSession;
import com.voltunity.evplatform.repository.ChargingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargingSessionService {

    @Autowired
    private ChargingSessionRepository chargingSessionRepository;

    public List<ChargingSession> getAllChargingSessions() {
        return chargingSessionRepository.findAll();
    }

    public ChargingSession getChargingSessionById(Long id) {
        return chargingSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Charging session not found with id: " + id));
    }

}