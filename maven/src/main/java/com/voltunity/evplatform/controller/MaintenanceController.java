package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.service.SlotService;
import com.voltunity.evplatform.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class MaintenanceController {

    @Autowired
    private SlotService slotService;

    @Autowired
    private StationService stationService;

    // GET /maintenance/plan?minSessions=10
    @GetMapping("/maintenance/plan")
    public ResponseEntity<List<Slot>> getMaintenancePlan(@RequestParam int minSessions) {
        List<Slot> plan = slotService.getMaintenancePlan(minSessions);
        return ResponseEntity.ok(plan);
    }

    // PUT /stations/{stationId}/pricing
    public static class PricingRequest {
        public double newPricePerKWh;
    }

    @PutMapping("/stations/{stationId}/pricing")
    public ResponseEntity<Station> updateStationPricing(
            @PathVariable Long stationId,
            @RequestBody PricingRequest request
    ) {
        Station station = stationService.updateStationPricing(stationId, request.newPricePerKWh);
        return ResponseEntity.ok(station);
    }
}