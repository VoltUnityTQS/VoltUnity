package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.service.SlotService;
import com.voltunity.evplatform.service.StationService;
import com.voltunity.evplatform.service.SecurityService;
import com.voltunity.evplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MaintenanceController {

    @Autowired
    private SlotService slotService;

    @Autowired
    private StationService stationService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/maintenance/plan")
    public ResponseEntity<List<Slot>> getMaintenancePlan(
            @RequestParam int minSessions,
            @RequestHeader("X-User-Id") Long currentUserId
    ) {
        User currentUser = userService.getUserById(currentUserId);

        if (securityService.isAdmin(currentUser)) {
            List<Slot> plan = slotService.getMaintenancePlan(minSessions);
            return ResponseEntity.ok(plan);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    public static class PricingRequest {
        public double newPricePerKWh;
    }

    @PutMapping("/stations/{stationId}/pricing")
    public ResponseEntity<Station> updateStationPricing(
            @PathVariable Long stationId,
            @RequestBody PricingRequest request,
            @RequestHeader("X-User-Id") Long currentUserId
    ) {
        User currentUser = userService.getUserById(currentUserId);

        if (securityService.isAdmin(currentUser)) {
            Station station = stationService.updateStationPricing(stationId, request.newPricePerKWh);
            return ResponseEntity.ok(station);
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}