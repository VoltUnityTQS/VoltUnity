package com.voltunity.evplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.SlotRepository;
import com.voltunity.evplatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ReservationControllerIT {

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
    }

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private SlotRepository slotRepository;

    private User testUser;
    private Slot testSlot;

    @BeforeEach
    void setUp() {
        slotRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setName("Bob");
        testUser.setEmail("bob@example.com");
        testUser.setPassword("secret");
        testUser.setRole("USER");
        testUser = userRepository.save(testUser);

        testSlot = new Slot();
        testSlot.setSlotStatus("AVAILABLE");
        testSlot = slotRepository.save(testSlot);
    }

    @Test
    public void testCreateReservation() throws Exception {
        ReservationController.BookingRequest bookingRequest = new ReservationController.BookingRequest();
        bookingRequest.setSlotId(testSlot.getId());
        bookingRequest.setUserId(testUser.getId());
        bookingRequest.setStart(LocalDateTime.now().plusHours(1));
        bookingRequest.setEnd(LocalDateTime.now().plusHours(2));

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isCreated());
    }
}
