package com.voltunity.evplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.repository.SlotRepository;
import com.voltunity.evplatform.repository.StationRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Testcontainers
public class SlotControllerIT {

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
    private SlotRepository slotRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Station testStation;

    @BeforeEach
    void setup() {
        slotRepository.deleteAll();
        stationRepository.deleteAll();

        testStation = new Station();
        testStation.setName("Test Station");
        testStation = stationRepository.save(testStation);
    }

    @Test
    void testCreateSlot() throws Exception {
        Slot slot = new Slot();
        slot.setSlotStatus("AVAILABLE");
        slot.setStation(testStation);

        mockMvc.perform(post("/api/v1/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(slot)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slotStatus").value("AVAILABLE"))
                .andExpect(jsonPath("$.station.id").value(testStation.getId()));
    }

    @Test
    void testGetAllSlots() throws Exception {
        Slot slot = new Slot();
        slot.setSlotStatus("AVAILABLE");
        slot.setStation(testStation);
        slotRepository.save(slot);

        mockMvc.perform(get("/api/v1/slots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testUpdateSlotStatus() throws Exception {
        Slot slot = new Slot();
        slot.setSlotStatus("AVAILABLE");
        slot.setStation(testStation);
        slot = slotRepository.save(slot);

        SlotController.SlotStatusUpdateRequest updateRequest = new SlotController.SlotStatusUpdateRequest();
        updateRequest.setSlotStatus("UNAVAILABLE");

        mockMvc.perform(put("/api/v1/slots/" + slot.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slotStatus").value("UNAVAILABLE"));
    }
}
