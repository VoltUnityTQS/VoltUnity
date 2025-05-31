package com.voltunity.evplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.voltunity.evplatform.model.Booking;
import com.voltunity.evplatform.service.BookingService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody ReservationRequest request) {
        Booking booking = bookingService.createBooking(
                request.getStationId(),
                request.getStartTimestamp(),
                request.getEndTimestamp(),
                request.getUserId()
        );
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long reservationId) {
        Booking booking = bookingService.getBooking(reservationId);
        return ResponseEntity.ok(booking);
    }
}

class ReservationRequest {
    private Long stationId;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;
    private Long userId;

    // Getters e Setters
    public Long getStationId() { return stationId; }
    public void setStationId(Long stationId) { this.stationId = stationId; }
    public LocalDateTime getStartTimestamp() { return startTimestamp; }
    public void setStartTimestamp(LocalDateTime startTimestamp) { this.startTimestamp = startTimestamp; }
    public LocalDateTime getEndTimestamp() { return endTimestamp; }
    public void setEndTimestamp(LocalDateTime endTimestamp) { this.endTimestamp = endTimestamp; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}