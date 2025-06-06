package com.voltunity.evplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voltunity.evplatform.model.Subscription;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.SubscriptionRepository;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Testcontainers
public class SubscriptionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

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

    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setup() {
        subscriptionRepository.deleteAll();
        userRepository.deleteAll();

        adminUser = new User();
        adminUser.setName("Admin");
        adminUser.setRole("ADMIN");
        adminUser = userRepository.save(adminUser);

        normalUser = new User();
        normalUser.setName("User");
        normalUser.setRole("USER");
        normalUser = userRepository.save(normalUser);
    }

    @Test
    void testCreateSubscription() throws Exception {
        SubscriptionController.SubscriptionRequest request = new SubscriptionController.SubscriptionRequest();
        request.userId = normalUser.getId();
        request.subscriptionType = "Basic";
        request.startDate = LocalDateTime.now();
        request.endDate = LocalDateTime.now().plusMonths(1);
        request.pricePerMonth = 29.99;
        request.sessionsIncluded = 10;
        request.discountPerKWh = 0.1;

        mockMvc.perform(post("/api/v1/subscriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subscriptionType").value("Basic"))
                .andExpect(jsonPath("$.pricePerMonth").value(29.99));
    }

    @Test
    void testGetAllSubscriptions() throws Exception {
        mockMvc.perform(get("/api/v1/subscriptions"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSubscriptionsByUserAsAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/subscriptions/users/" + normalUser.getId())
                        .header("X-User-Id", adminUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSubscriptionsByUserAsSameUser() throws Exception {
        mockMvc.perform(get("/api/v1/subscriptions/users/" + normalUser.getId())
                        .header("X-User-Id", normalUser.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetSubscriptionsByUserForbidden() throws Exception {
        User anotherUser = new User();
        anotherUser.setName("Another");
        anotherUser.setRole("USER");
        anotherUser = userRepository.save(anotherUser);

        mockMvc.perform(get("/api/v1/subscriptions/users/" + normalUser.getId())
                        .header("X-User-Id", anotherUser.getId()))
                .andExpect(status().isForbidden());
    }
}
