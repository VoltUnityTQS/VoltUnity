package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.service.ChargingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/charges")
@CrossOrigin(origins = "*")
public class ChargingController {

    @Autowired
    private ChargingService chargingService;

    // DTO para desbloqueio
    public static class UnlockRequest {
        private Long bookingId;
        private Long stationId;
        private Long slotId;

        public Long getBookingId() {
            return bookingId;
        }

        public void setBookingId(Long bookingId) {
            this.bookingId = bookingId;
        }

        public Long getStationId() {
            return stationId;
        }

        public void setStationId(Long stationId) {
            this.stationId = stationId;
        }

        public Long getSlotId() {
            return slotId;
        }

        public void setSlotId(Long slotId) {
            this.slotId = slotId;
        }
    }

    // POST /api/v1/charges/unlock
    @PostMapping("/unlock")
    public ResponseEntity<String> unlockSlot(@RequestBody UnlockRequest request) {
        try {
            boolean unlocked = chargingService.unlockSlot(request.getBookingId(), request.getStationId(), request.getSlotId());
            if (unlocked) {
                return ResponseEntity.ok("Slot desbloqueado com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível desbloquear o slot.");
            }
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}