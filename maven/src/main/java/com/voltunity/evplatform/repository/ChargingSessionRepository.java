package com.voltunity.evplatform.repository;

import com.voltunity.evplatform.model.ChargingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Long> {

    List<ChargingSession> findBySlot_Id(Long slotId);

    List<ChargingSession> findBySlot_Station_IdAndUser_IdAndStartTimestampBetween(
            Long stationId, Long userId, LocalDateTime start, LocalDateTime end);

    List<ChargingSession> findBySlot_Station_IdAndStartTimestampBetween(
            Long stationId, LocalDateTime start, LocalDateTime end);

    List<ChargingSession> findByUser_IdAndStartTimestampBetween(
            Long userId, LocalDateTime start, LocalDateTime end);

    List<ChargingSession> findByStartTimestampBetween(
            LocalDateTime start, LocalDateTime end);
}