package com.voltunity.evplatform.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.voltunity.evplatform.model.Booking;
import com.voltunity.evplatform.model.ChargerPoint;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.chargerPoint = :chargerPoint AND " +
           "(:start < b.end AND :end > b.start)")
    List<Booking> findByChargerPointAndTimeOverlap(
            @Param("chargerPoint") ChargerPoint chargerPoint,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
