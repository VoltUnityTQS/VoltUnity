package com.voltunity.evplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.voltunity.evplatform.model.Station;

public interface StationRepository extends JpaRepository<Station, Long> {}