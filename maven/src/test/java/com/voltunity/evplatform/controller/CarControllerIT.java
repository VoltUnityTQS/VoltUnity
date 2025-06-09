package com.voltunity.evplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltunity.evplatform.model.Car;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.CarRepository;
import com.voltunity.evplatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Testcontainers
public class CarControllerIT {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    private User testUser;
    private User adminUser;
    private User otherUser;
    private Car testCar;

    @BeforeEach
    void setUp() {
        carRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User("User", "user@example.com", "123", "USER");
        adminUser = new User("Admin", "admin@example.com", "admin", "ADMIN");
        otherUser = new User("Other", "other@example.com", "pass", "USER");

        testUser = userRepository.save(testUser);
        adminUser = userRepository.save(adminUser);
        otherUser = userRepository.save(otherUser);

        testCar = new Car();
        testCar.setMake("Tesla");
        testCar.setModel("Model 3");
        testCar.setLicensePlate("ABC-1234");
        testCar.setUser(testUser);
        testCar = carRepository.save(testCar);
    }

    @Test
    void testAddCar() throws Exception {
        CarController.CarRequest request = new CarController.CarRequest();
        request.make = "Nissan";
        request.model = "Leaf";
        request.licensePlate = "XYZ-9876";
        request.userId = testUser.getId();

        mockMvc.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.make").value("Nissan"))
                .andExpect(jsonPath("$.licensePlate").value("XYZ-9876"));
    }

    @Test
    void testGetCarsByUser_OwnUser() throws Exception {
        mockMvc.perform(get("/api/v1/cars/users/" + testUser.getId())
                        .header("X-User-Id", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].make").value("Tesla"));
    }

    @Test
    void testGetCarsByUser_AsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/cars/users/" + testUser.getId())
                        .header("X-User-Id", adminUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].model").value("Model 3"));
    }

    @Test
    void testGetCarsByUser_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/cars/users/" + testUser.getId())
                        .header("X-User-Id", otherUser.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllCars() throws Exception {
        mockMvc.perform(get("/api/v1/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].licensePlate").value("ABC-1234"));
    }

    @Test
    void testDeleteCar() throws Exception {
        mockMvc.perform(delete("/api/v1/cars/" + testCar.getId())
                        .header("X-User-Id", testUser.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/cars"))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
