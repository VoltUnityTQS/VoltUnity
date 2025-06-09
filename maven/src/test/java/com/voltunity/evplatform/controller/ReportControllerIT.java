package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.service.ReportService;
import com.voltunity.evplatform.service.ReportService.ConsumptionReport;
import com.voltunity.evplatform.service.ReportService.CO2ImpactReport;
import com.voltunity.evplatform.service.ReportService.SustainabilityReport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
public class ReportControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    private final String startParam = "2024-01-01T00:00:00";
    private final String endParam = "2024-12-31T23:59:00";

    @BeforeEach
    void setup() {
        Mockito.reset(reportService);
    }

    @Test
    void testGetConsumptionReport() throws Exception {
        Mockito.when(reportService.getConsumptionReport(any(), any(), any(), any()))
                .thenReturn(new ConsumptionReport(100.0, 30.0));

        mockMvc.perform(get("/api/v1/reports/consumption")
                        .param("startDate", startParam)
                        .param("endDate", endParam))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEnergyKWh", is(100.0)))
                .andExpect(jsonPath("$.totalCost", is(30.0)));
    }

    @Test
    void testGetCO2ImpactReport() throws Exception {
        Mockito.when(reportService.getCO2ImpactReport(any(), any(), any(), any()))
                .thenReturn(new CO2ImpactReport(50.0, 20.0));

        mockMvc.perform(get("/api/v1/reports/co2")
                        .param("startDate", startParam)
                        .param("endDate", endParam))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEnergyKWh", is(50.0)))
                .andExpect(jsonPath("$.totalCO2SavedKg", is(20.0)));
    }

    @Test
    void testGetCO2ImpactForUser() throws Exception {
        Mockito.when(reportService.getCO2ImpactForUser(eq(10L), any(), any()))
                .thenReturn(new CO2ImpactReport(25.0, 10.0));

        mockMvc.perform(get("/api/v1/reports/users/10/co2")
                        .param("startDate", startParam)
                        .param("endDate", endParam))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEnergyKWh", is(25.0)))
                .andExpect(jsonPath("$.totalCO2SavedKg", is(10.0)));
    }

    @Test
    void testGetSustainabilityReport() throws Exception {
        Mockito.when(reportService.getSustainabilityReport(any(), any()))
                .thenReturn(new SustainabilityReport(120.0, 48.0, 10, 5, 3));

        mockMvc.perform(get("/api/v1/reports/sustainability")
                        .param("startDate", startParam)
                        .param("endDate", endParam))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEnergyKWh", is(120.0)))
                .andExpect(jsonPath("$.totalCO2SavedKg", is(48.0)))
                .andExpect(jsonPath("$.totalSessions", is(10)))
                .andExpect(jsonPath("$.totalUsers", is(5)))
                .andExpect(jsonPath("$.totalStations", is(3)));
    }
}
