package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Car;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserService userService;

    public Car addCar(Long userId, Car car) {
        User user = userService.getUserById(userId);
        car.setUser(user);
        return carRepository.save(car);
    }

    public List<Car> getCarsByUser(Long userId) {
        return carRepository.findByUser_Id(userId);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Carro não encontrado com id: " + carId));
    }

    public void deleteCar(Long carId) {
        carRepository.deleteById(carId);
    }
}