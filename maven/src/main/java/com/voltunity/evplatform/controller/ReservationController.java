package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.model.Booking;
import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.BookingRepository;
import com.voltunity.evplatform.service.BookingService;
import com.voltunity.evplatform.service.SlotService;
import com.voltunity.evplatform.service.UserService;
import com.voltunity.evplatform.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private SlotService slotService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    // DTO para criar reserva
    public static class BookingRequest {
        private Long slotId;
        private Long userId;
        private LocalDateTime start;
        private LocalDateTime end;

        // Getters e setters
        public Long getSlotId() { return slotId; }
        public void setSlotId(Long slotId) { this.slotId = slotId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public LocalDateTime getStart() { return start; }
        public void setStart(LocalDateTime start) { this.start = start; }
        public LocalDateTime getEnd() { return end; }
        public void setEnd(LocalDateTime end) { this.end = end; }
    }

    // POST /reservations
    @PostMapping
    public ResponseEntity<Booking> createReservation(@RequestBody BookingRequest request) {
        Slot slot = slotService.getSlotById(request.getSlotId());
        User user = userService.getUserById(request.getUserId());

        if (!slot.getSlotStatus().equalsIgnoreCase("AVAILABLE")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        slot.setSlotStatus("IN_USE");
        slotService.updateSlot(slot);

        Booking booking = new Booking();
        booking.setSlot(slot);
        booking.setUser(user);
        booking.setBookingStatus("confirmed");
        booking.setStart(request.getStart());
        booking.setEnd_time(request.getEnd());
        booking.setPriceAtBooking(0.0f);

        Booking savedBooking = bookingService.saveBooking(booking);

        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);
    }

    // PUT /reservations/{id}/cancel
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long currentUserId
    ) {
        User currentUser = userService.getUserById(currentUserId);
        Booking booking = bookingService.getBooking(id);

        if (securityService.isAdmin(currentUser) || securityService.isSameUser(currentUser, booking.getUser().getId())) {
            Booking cancelled = bookingService.cancelBooking(id);
            return ResponseEntity.ok(cancelled);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping
    public List<Booking> getAllReservations() {
        return bookingService.getAllBookings();
    }

}