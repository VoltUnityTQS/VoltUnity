package com.voltunity.evplatform.repository;

import com.voltunity.evplatform.model.ChargingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Long> {
}