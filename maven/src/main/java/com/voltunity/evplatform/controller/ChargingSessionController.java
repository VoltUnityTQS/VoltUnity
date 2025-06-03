package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.model.ChargingSession;
import com.voltunity.evplatform.service.ChargingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chargingSessions")
@CrossOrigin(origins = "*")
public class ChargingSessionController {

    @Autowired
    private ChargingSessionService chargingSessionService;

    // DTO para iniciar sessão
    public static class StartSessionRequest {
        private Long slotId;
        private Long userId;

        public Long getSlotId() {
            return slotId;
        }

        public void setSlotId(Long slotId) {
            this.slotId = slotId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }

    // DTO para finalizar sessão
    public static class EndSessionRequest {
        private double energyConsumedKWh;

        public double getEnergyConsumedKWh() {
            return energyConsumedKWh;
        }

        public void setEnergyConsumedKWh(double energyConsumedKWh) {
            this.energyConsumedKWh = energyConsumedKWh;
        }
    }

    // POST /chargingSessions → iniciar sessão
    @PostMapping
    public ResponseEntity<ChargingSession> startSession(@RequestBody StartSessionRequest request) {
        ChargingSession session = chargingSessionService.startSession(request.getSlotId(), request.getUserId());
        return new ResponseEntity<>(session, HttpStatus.CREATED);
    }

    // PUT /chargingSessions/{id} → finalizar sessão
    @PutMapping("/{id}")
    public ResponseEntity<ChargingSession> endSession(@PathVariable Long id, @RequestBody EndSessionRequest request) {
        ChargingSession session = chargingSessionService.endSession(id, request.getEnergyConsumedKWh());
        return ResponseEntity.ok(session);
    }

    // GET /chargingSessions → listar sessões
    @GetMapping
    public ResponseEntity<List<ChargingSession>> getAllSessions() {
        List<ChargingSession> sessions = chargingSessionService.getAllChargingSessions();
        return ResponseEntity.ok(sessions);
    }
}