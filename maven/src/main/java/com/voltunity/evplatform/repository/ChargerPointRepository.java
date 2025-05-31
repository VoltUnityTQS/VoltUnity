package com.voltunity.evplatform.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.voltunity.evplatform.model.ChargerPoint;

public interface ChargerPointRepository extends JpaRepository<ChargerPoint, Long> {
    List<ChargerPoint> findByStationId(Long stationId);
}