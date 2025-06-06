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
public class StationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private ObjectMapper objectMapper;

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

    private Station testStation;

    @BeforeEach
    void setup() {
        slotRepository.deleteAll();
        stationRepository.deleteAll();

        testStation = new Station();
        testStation.setName("Test Station");
        testStation.setLat((float)40.0);
        testStation.setLng((float)-8.0);
        testStation.setStationStatus("ACTIVE");
        testStation = stationRepository.save(testStation);
    }

    @Test
    void testCreateStation() throws Exception {
        Station station = new Station();
        station.setName("Nova Estação");
        station.setLat((float)41.0);
        station.setLng((float)-7.0);

        mockMvc.perform(post("/api/v1/stations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(station)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Nova Estação"))
                .andExpect(jsonPath("$.lat").value(41.0));
    }

    @Test
    void testGetAllStations() throws Exception {
        mockMvc.perform(get("/api/v1/stations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetSlotsByStation() throws Exception {
        Slot slot = new Slot();
        slot.setSlotStatus("AVAILABLE");
        slot.setStation(testStation);
        slotRepository.save(slot);

        mockMvc.perform(get("/api/v1/stations/" + testStation.getId() + "/slots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("[0].slotStatus").value("AVAILABLE"));
    }

    @Test
    void testSearchStations() throws Exception {
        mockMvc.perform(get("/api/v1/stations/search")
                        .param("latitude", "40")
                        .param("longitude", "-8")
                        .param("radius", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testUpdateStation() throws Exception {
        testStation.setName("Estação Atualizada");

        mockMvc.perform(put("/api/v1/stations/" + testStation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Estação Atualizada"));
    }

    @Test
    void testUpdateStationStatus() throws Exception {
        StationController.StationStatusUpdateRequest request = new StationController.StationStatusUpdateRequest();
        request.setStationStatus("INACTIVE");

        mockMvc.perform(put("/api/v1/stations/" + testStation.getId() + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationStatus").value("INACTIVE"));
    }
}
