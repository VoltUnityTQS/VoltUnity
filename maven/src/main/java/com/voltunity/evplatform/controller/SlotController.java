package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/slots")
@CrossOrigin(origins = "*") // opcional para React
public class SlotController {

    @Autowired
    private SlotService slotService;

    // POST /slots → criar um slot
    @PostMapping
    public ResponseEntity<Slot> createSlot(@RequestBody Slot slot) {
        Slot savedSlot = slotService.saveSlot(slot);
        return new ResponseEntity<>(savedSlot, HttpStatus.CREATED);
    }

    // GET /slots → listar todos os slots
    @GetMapping
    public ResponseEntity<List<Slot>> getAllSlots() {
        List<Slot> slots = slotService.getAllSlots();
        return new ResponseEntity<>(slots, HttpStatus.OK);
    }

    // DTO para atualizar o status
    public static class SlotStatusUpdateRequest {
        private String slotStatus;

        public String getSlotStatus() {
            return slotStatus;
        }

        public void setSlotStatus(String slotStatus) {
            this.slotStatus = slotStatus;
        }
    }

    // PUT /slots/{id}/status → atualizar status do slot
    @PutMapping("/{id}/status")
    public ResponseEntity<Slot> updateSlotStatus(@PathVariable Long id, @RequestBody SlotStatusUpdateRequest request) {
        Slot updatedSlot = slotService.updateSlotStatus(id, request.getSlotStatus());
        return new ResponseEntity<>(updatedSlot, HttpStatus.OK);
    }
}