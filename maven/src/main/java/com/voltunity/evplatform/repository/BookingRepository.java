package com.voltunity.evplatform.repository;

import com.voltunity.evplatform.model.Booking;
import com.voltunity.evplatform.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

       @Query("SELECT b FROM Booking b WHERE b.slot = :slot AND " +
            "(:start < b.end AND :end > b.start)")
    List<Booking> findBySlotAndTimeOverlap(Slot slot, LocalDateTime start, LocalDateTime end);


       @Query("SELECT b FROM Booking b WHERE b.slot = :slot " +
              "AND b.bookingStatus = 'confirmed' " +
              "AND :now BETWEEN b.start AND b.end")
       Optional<Booking> findValidBookingForSlot(Slot slot, LocalDateTime now);

    List<Booking> findByUser_Id(Long userId);
}

