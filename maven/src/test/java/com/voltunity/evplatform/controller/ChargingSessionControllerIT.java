package com.voltunity.evplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltunity.evplatform.model.*;
import com.voltunity.evplatform.repository.ChargingSessionRepository;
import com.voltunity.evplatform.repository.SlotRepository;
import com.voltunity.evplatform.repository.StationRepository;
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

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ChargingSessionControllerIT {

    @Autowired private UserRepository userRepository;
    @Autowired private SlotRepository slotRepository;
    @Autowired private StationRepository stationRepository;
    @Autowired private ChargingSessionRepository chargingSessionRepository;

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

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

    private User user;
    private Slot slot;

    @BeforeEach
    void setup() {
        // Limpar dados antigos
        chargingSessionRepository.deleteAll();
        slotRepository.deleteAll();
        stationRepository.deleteAll();
        userRepository.deleteAll();

        // Criar e salvar entidades necessárias
        user = new User("Alice", "alice@example.com","user123", "USER");
        user = userRepository.save(user);

        Station station = new Station();
        station.setName("Test Station");
        station.setStationStatus("ACTIVE");
        station.setLat((float) 40.0);
        station.setLng((float) -8.0);
        station.setAddress("Rua Teste");
        station.setTotalSlots(4);
        station.setMaxPower((float) 22.0);
        station.setPricePerKWh(0.3);
        station = stationRepository.save(station);

        slot = new Slot();
        slot.setSlotStatus("AVAILABLE");
        slot.setPower((float) 22.0);
        slot.setStation(station);
        slot = slotRepository.save(slot);

        // Criar sessão de carregamento para os testes de GET
        ChargingSession session = new ChargingSession();
        session.setUser(user);
        session.setSlot(slot);
        session.setStartTimestamp(LocalDateTime.now().minusHours(1));
        session.setEndTimestamp(LocalDateTime.now());
        session.setEnergyConsumedKWh(10.0);
        chargingSessionRepository.save(session);
    }

    @Test
    public void testGetAllChargingSessions() throws Exception {
        mockMvc.perform(get("/api/v1/charging-sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    public void testCreateChargingSession() throws Exception {
        var request = new ChargingSessionController.StartSessionRequest();
        request.setUserId(user.getId());
        request.setSlotId(slot.getId());

        mockMvc.perform(post("/api/v1/charging-sessions")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetChargingSessionsByUser() throws Exception {
        mockMvc.perform(get("/api/v1/charging-sessions/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }
}
