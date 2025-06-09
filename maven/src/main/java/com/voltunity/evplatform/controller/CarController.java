package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.model.Car;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.service.CarService;
import com.voltunity.evplatform.service.SecurityService;
import com.voltunity.evplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
@CrossOrigin(origins = "*")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    public static class CarRequest {
        public String make;
        public String model;
        public String licensePlate;
        public Long userId;
    }

    @PostMapping
    public ResponseEntity<Car> addCar(@RequestBody CarRequest request) {
        Car car = new Car();
        car.setMake(request.make);
        car.setModel(request.model);
        car.setLicensePlate(request.licensePlate);

        Car savedCar = carService.addCar(request.userId, car);
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Car>> getCarsByUser(
            @PathVariable Long userId,
            @RequestHeader("X-User-Id") Long currentUserId
    ) {
        User currentUser = userService.getUserById(currentUserId);

        if (securityService.isAdmin(currentUser) || securityService.isSameUser(currentUser, userId)) {
            return ResponseEntity.ok(carService.getCarsByUser(userId));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<Void> deleteCar(
            @PathVariable Long carId,
            @RequestHeader("X-User-Id") Long currentUserId
    ) {
        carService.deleteCar(carId);
        return ResponseEntity.noContent().build();
    }
}