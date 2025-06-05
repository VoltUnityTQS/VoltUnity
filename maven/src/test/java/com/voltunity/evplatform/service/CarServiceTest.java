
package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Car;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCar_shouldAddCarForUser() {
        User user = new User();
        user.setId(1L);

        Car car = new Car();
        car.setMake("Tesla");
        car.setModel("Model 3");
        car.setLicensePlate("11-AA-22");

        when(userService.getUserById(1L)).thenReturn(user);
        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Car result = carService.addCar(1L, car);

        assertEquals(user, result.getUser());
        assertEquals("Tesla", result.getMake());
        assertEquals("Model 3", result.getModel());
        assertEquals("11-AA-22", result.getLicensePlate());
    }

    @Test
    void testGetCarById_shouldReturnCar() {
        Car car = new Car();
        car.setId(1L);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        Car result = carService.getCarById(1L);

        assertEquals(1L, result.getId());
    }
}