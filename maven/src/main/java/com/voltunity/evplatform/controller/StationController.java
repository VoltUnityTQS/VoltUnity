package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stations")
@CrossOrigin(origins = "*") // opcional, se tiveres frontend React
public class StationController {

    @Autowired
    private StationService stationService;

    // POST /stations
    @PostMapping
    public ResponseEntity<Station> createStation(@Valid @RequestBody Station station) {
        Station savedStation = stationService.saveStation(station);
        return new ResponseEntity<>(savedStation, HttpStatus.CREATED);
    }

    // GET /stations
    @GetMapping
    public ResponseEntity<List<Station>> getAllStations() {
        List<Station> stations = stationService.getAllStations();
        return new ResponseEntity<>(stations, HttpStatus.OK);
    }

    // DTO para receber o novo status
    public static class StationStatusUpdateRequest {
        private String stationStatus;

        public String getStationStatus() {
            return stationStatus;
        }

        public void setStationStatus(String stationStatus) {
            this.stationStatus = stationStatus;
        }
    }

    // PUT /stations/{id}/status
    @PutMapping("/{id}/status")
    public ResponseEntity<Station> updateStationStatus(@PathVariable Long id, @RequestBody StationStatusUpdateRequest request) {
        Station updatedStation = stationService.updateStationStatus(id, request.getStationStatus());
        return new ResponseEntity<>(updatedStation, HttpStatus.OK);
    }
}