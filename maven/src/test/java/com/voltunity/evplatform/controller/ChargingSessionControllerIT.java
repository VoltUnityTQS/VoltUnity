package com.voltunity.evplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltunity.evplatform.model.ChargingSession;
import com.voltunity.evplatform.repository.SlotRepository;
import com.voltunity.evplatform.repository.UserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ChargingSessionControllerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SlotRepository slotRepository;

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
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllChargingSessions() throws Exception {
        mockMvc.perform(get("/api/v1/charging-sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    public void testCreateChargingSession() throws Exception {
        ChargingSession session = new ChargingSession();
        session.setUser(userRepository.findById(1L).orElseThrow());
        session.setSlot(slotRepository.findById(1L).orElseThrow());
        session.setSessionStatus("ACTIVE");
        session.setStartTimestamp(LocalDateTime.now().minusMinutes(30));
        session.setEndTimestamp(LocalDateTime.now());
        session.setEnergyConsumedKWh(12.5); // usa o nome certo do teu model

        mockMvc.perform(post("/api/v1/charging-sessions")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetChargingSessionsByUser() throws Exception {
        Long userId = 1L;

        mockMvc.perform(get("/api/v1/charging-sessions/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }
}