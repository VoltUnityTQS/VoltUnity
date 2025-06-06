package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.ChargingSession;
import com.voltunity.evplatform.repository.ChargingSessionRepository;
import com.voltunity.evplatform.repository.StationRepository;
import com.voltunity.evplatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private ChargingSessionRepository chargingSessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private ReportService reportService;

    private final LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
    private final LocalDateTime end = LocalDateTime.of(2024, 12, 31, 23, 59);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ChargingSession mockSession(double kWh, String status) {
        ChargingSession s = new ChargingSession();
        s.setEnergyConsumedKWh(kWh);
        s.setSessionStatus(status);
        return s;
    }

    @Test
    void testGetConsumptionReport_noFilters() {
        List<ChargingSession> sessions = List.of(
                mockSession(10.0, "COMPLETED"),
                mockSession(5.0, "CANCELLED"),
                mockSession(15.0, "COMPLETED")
        );

        when(chargingSessionRepository.findByStartTimestampBetween(start, end)).thenReturn(sessions);

        var report = reportService.getConsumptionReport(null, null, start, end);
        assertThat(report.getTotalEnergyKWh()).isEqualTo(25.0);
        assertThat(report.getTotalCost()).isEqualTo(25.0 * 0.30);
    }

    @Test
    void testGetCO2ImpactReport_stationFilter() {
        List<ChargingSession> sessions = List.of(
                mockSession(20.0, "COMPLETED"),
                mockSession(5.0, "FAILED")
        );

        when(chargingSessionRepository.findBySlot_Station_IdAndStartTimestampBetween(1L, start, end)).thenReturn(sessions);

        var report = reportService.getCO2ImpactReport(1L, null, start, end);
        assertThat(report.getTotalEnergyKWh()).isEqualTo(20.0);
        assertThat(report.getTotalCO2SavedKg()).isEqualTo(20.0 * 0.4);
    }

    @Test
    void testGetCO2ImpactForUser() {
        List<ChargingSession> sessions = List.of(
                mockSession(12.5, "COMPLETED"),
                mockSession(3.5, "IN_PROGRESS")
        );

        when(chargingSessionRepository.findByUser_IdAndStartTimestampBetween(99L, start, end)).thenReturn(sessions);

        var report = reportService.getCO2ImpactForUser(99L, start, end);
        assertThat(report.getTotalEnergyKWh()).isEqualTo(12.5);
        assertThat(report.getTotalCO2SavedKg()).isEqualTo(12.5 * 0.4);
    }

    @Test
    void testGetSustainabilityReport() {
        List<ChargingSession> sessions = List.of(
                mockSession(40.0, "COMPLETED"),
                mockSession(10.0, "FAILED"),
                mockSession(20.0, "COMPLETED")
        );

        when(chargingSessionRepository.findByStartTimestampBetween(start, end)).thenReturn(sessions);
        when(userRepository.count()).thenReturn(50L);
        when(stationRepository.count()).thenReturn(10L);

        var report = reportService.getSustainabilityReport(start, end);
        assertThat(report.getTotalEnergyKWh()).isEqualTo(60.0);
        assertThat(report.getTotalCO2SavedKg()).isEqualTo(60.0 * 0.4);
        assertThat(report.getTotalSessions()).isEqualTo(3);
        assertThat(report.getTotalUsers()).isEqualTo(50L);
        assertThat(report.getTotalStations()).isEqualTo(10L);
    }
}
