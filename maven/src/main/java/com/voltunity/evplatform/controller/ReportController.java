package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.service.ReportService;
import com.voltunity.evplatform.service.ReportService.ConsumptionReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // GET /reports/consumption
    @GetMapping("/consumption")
    public ResponseEntity<ConsumptionReport> getConsumptionReport(
            @RequestParam(required = false) Long stationId,
            @RequestParam(required = false) Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        ConsumptionReport report = reportService.getConsumptionReport(stationId, userId, startDate, endDate);
        return ResponseEntity.ok(report);
    }


    @GetMapping("/co2")
    public ResponseEntity<ReportService.CO2ImpactReport> getCO2ImpactReport(
            @RequestParam(required = false) Long stationId,
            @RequestParam(required = false) Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        ReportService.CO2ImpactReport report = reportService.getCO2ImpactReport(stationId, userId, startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/users/{userId}/co2")
    public ResponseEntity<ReportService.CO2ImpactReport> getCO2ImpactForUser(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        ReportService.CO2ImpactReport report = reportService.getCO2ImpactForUser(userId, startDate, endDate);
        return ResponseEntity.ok(report);
    }
}