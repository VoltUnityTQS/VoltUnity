package com.voltunity.evplatform.repository;

import com.voltunity.evplatform.model.ChargingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.voltunity.evplatform.model.User;
import java.util.List;


@Repository
public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Long> {
    List<ChargingSession> findByUser(User user);
}