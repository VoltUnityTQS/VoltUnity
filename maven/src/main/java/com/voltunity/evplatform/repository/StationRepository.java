package com.voltunity.evplatform.repository;

import com.voltunity.evplatform.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    // Busca por localização (distância em metros), com filtros opcionais
    @Query(value = """
        SELECT s.* FROM station s
        WHERE (
            6371000 * acos(
                cos(radians(:latitude)) * cos(radians(s.lat)) *
                cos(radians(s.lng) - radians(:longitude)) +
                sin(radians(:latitude)) * sin(radians(s.lat))
            )
        ) <= :radius
        AND (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """, nativeQuery = true)
    List<Station> findStationsByLocationAndName(
            double latitude,
            double longitude,
            double radius,
            String name
    );
}