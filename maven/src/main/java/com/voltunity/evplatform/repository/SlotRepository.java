package com.voltunity.evplatform.repository;

import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {

    List<Slot> findByStation(Station station);
}