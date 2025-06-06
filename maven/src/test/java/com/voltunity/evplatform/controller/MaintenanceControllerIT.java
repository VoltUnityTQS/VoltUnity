package com.voltunity.evplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltunity.evplatform.model.ChargingSession;
import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.ChargingSessionRepository;
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

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class MaintenanceControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private SlotRepository slotRepository;
    @Autowired private ChargingSessionRepository chargingSessionRepository;


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

    private User adminUser;
    private Slot testSlot;

    @BeforeEach
    void setUp() {
        chargingSessionRepository.deleteAll();
        slotRepository.deleteAll();
        userRepository.deleteAll();

        Slot slot = new Slot();
        slotRepository.save(slot);

        adminUser = new User();
        adminUser.setName("Charlie");
        adminUser.setEmail("charlie@example.com");
        adminUser.setPassword("admin123");
        adminUser.setRole("ADMIN");
        adminUser = userRepository.save(adminUser);

        ChargingSession session = new ChargingSession();
        session.setUser(adminUser);
        session.setSlot(slot);
        session.setStartTimestamp(LocalDateTime.now().minusHours(2));
        session.setEndTimestamp(LocalDateTime.now().minusHours(1));
        session.setSessionStatus("COMPLETED");
        chargingSessionRepository.save(session);

        testSlot = new Slot();
        testSlot.setSlotStatus("AVAILABLE");
        testSlot = slotRepository.save(testSlot);
    }

    @Test
    public void testGetMaintenancePlanAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/maintenance/plan")
                        .param("minSessions", "1")
                        .header("X-User-Id", adminUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }
}
