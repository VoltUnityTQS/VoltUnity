package com.voltunity.evplatform.repository;

import com.voltunity.evplatform.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByUser_Id(Long userId);
    boolean existsByLicensePlate(String licensePlate);


}