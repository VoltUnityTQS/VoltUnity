package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.ChargingSession;
import com.voltunity.evplatform.repository.ChargingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.voltunity.evplatform.repository.UserRepository;
import com.voltunity.evplatform.repository.StationRepository;

@Service
public class ReportService {

    @Autowired
    private ChargingSessionRepository chargingSessionRepository;

    // Tarifa (€/kWh) — depois podes parametrizar
    private static final double COST_PER_KWH = 0.30;

    public ConsumptionReport getConsumptionReport(Long stationId, Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<ChargingSession> sessions;

        if (stationId != null && userId != null) {
            sessions = chargingSessionRepository.findBySlot_Station_IdAndUser_IdAndStartTimestampBetween(
                    stationId, userId, startDate, endDate);
        } else if (stationId != null) {
            sessions = chargingSessionRepository.findBySlot_Station_IdAndStartTimestampBetween(
                    stationId, startDate, endDate);
        } else if (userId != null) {
            sessions = chargingSessionRepository.findByUser_IdAndStartTimestampBetween(
                    userId, startDate, endDate);
        } else {
            sessions = chargingSessionRepository.findByStartTimestampBetween(startDate, endDate);
        }

        double totalEnergy = sessions.stream()
                .filter(s -> s.getSessionStatus().equalsIgnoreCase("COMPLETED"))
                .mapToDouble(ChargingSession::getEnergyConsumedKWh)
                .sum();

        double totalCost = totalEnergy * COST_PER_KWH;

        return new ConsumptionReport(totalEnergy, totalCost);
    }

    // DTO interno
    public static class ConsumptionReport {
        private double totalEnergyKWh;
        private double totalCost;

        public ConsumptionReport(double totalEnergyKWh, double totalCost) {
            this.totalEnergyKWh = totalEnergyKWh;
            this.totalCost = totalCost;
        }

        public double getTotalEnergyKWh() {
            return totalEnergyKWh;
        }

        public void setTotalEnergyKWh(double totalEnergyKWh) {
            this.totalEnergyKWh = totalEnergyKWh;
        }

        public double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(double totalCost) {
            this.totalCost = totalCost;
        }
    }

    public CO2ImpactReport getCO2ImpactReport(Long stationId, Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<ChargingSession> sessions;

        if (stationId != null && userId != null) {
            sessions = chargingSessionRepository.findBySlot_Station_IdAndUser_IdAndStartTimestampBetween(
                    stationId, userId, startDate, endDate);
        } else if (stationId != null) {
            sessions = chargingSessionRepository.findBySlot_Station_IdAndStartTimestampBetween(
                    stationId, startDate, endDate);
        } else if (userId != null) {
            sessions = chargingSessionRepository.findByUser_IdAndStartTimestampBetween(
                    userId, startDate, endDate);
        } else {
            sessions = chargingSessionRepository.findByStartTimestampBetween(startDate, endDate);
        }

        double totalEnergy = sessions.stream()
                .filter(s -> s.getSessionStatus().equalsIgnoreCase("COMPLETED"))
                .mapToDouble(ChargingSession::getEnergyConsumedKWh)
                .sum();


        double FACTOR_CO2_KWH = 0.4;
        double totalCO2 = totalEnergy * FACTOR_CO2_KWH;

        return new CO2ImpactReport(totalEnergy, totalCO2);
    }

    public static class CO2ImpactReport {
        private double totalEnergyKWh;
        private double totalCO2SavedKg;

        public CO2ImpactReport(double totalEnergyKWh, double totalCO2SavedKg) {
            this.totalEnergyKWh = totalEnergyKWh;
            this.totalCO2SavedKg = totalCO2SavedKg;
        }

        public double getTotalEnergyKWh() {
            return totalEnergyKWh;
        }

        public void setTotalEnergyKWh(double totalEnergyKWh) {
            this.totalEnergyKWh = totalEnergyKWh;
        }

        public double getTotalCO2SavedKg() {
            return totalCO2SavedKg;
        }

        public void setTotalCO2SavedKg(double totalCO2SavedKg) {
            this.totalCO2SavedKg = totalCO2SavedKg;
        }
    }

    public CO2ImpactReport getCO2ImpactForUser(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<ChargingSession> sessions = chargingSessionRepository.findByUser_IdAndStartTimestampBetween(
                userId, startDate, endDate);

        double totalEnergy = sessions.stream()
                .filter(s -> s.getSessionStatus().equalsIgnoreCase("COMPLETED"))
                .mapToDouble(ChargingSession::getEnergyConsumedKWh)
                .sum();

        double FACTOR_CO2_KWH = 0.4;
        double totalCO2 = totalEnergy * FACTOR_CO2_KWH;

        return new CO2ImpactReport(totalEnergy, totalCO2);
    }


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StationRepository stationRepository;

    public SustainabilityReport getSustainabilityReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<ChargingSession> sessions = chargingSessionRepository.findByStartTimestampBetween(startDate, endDate);

        double totalEnergy = sessions.stream()
                .filter(s -> s.getSessionStatus().equalsIgnoreCase("COMPLETED"))
                .mapToDouble(ChargingSession::getEnergyConsumedKWh)
                .sum();

        double FACTOR_CO2_KWH = 0.4;
        double totalCO2 = totalEnergy * FACTOR_CO2_KWH;

        long totalSessions = sessions.size();
        long totalUsers = userRepository.count();
        long totalStations = stationRepository.count();

        return new SustainabilityReport(totalEnergy, totalCO2, totalSessions, totalUsers, totalStations);
    }

    public static class SustainabilityReport {
    private double totalEnergyKWh;
    private double totalCO2SavedKg;
    private long totalSessions;
    private long totalUsers;
    private long totalStations;

    public SustainabilityReport(double totalEnergyKWh, double totalCO2SavedKg, long totalSessions, long totalUsers, long totalStations) {
        this.totalEnergyKWh = totalEnergyKWh;
        this.totalCO2SavedKg = totalCO2SavedKg;
        this.totalSessions = totalSessions;
        this.totalUsers = totalUsers;
        this.totalStations = totalStations;
    }

    public double getTotalEnergyKWh() {
        return totalEnergyKWh;
    }

    public void setTotalEnergyKWh(double totalEnergyKWh) {
        this.totalEnergyKWh = totalEnergyKWh;
    }

    public double getTotalCO2SavedKg() {
        return totalCO2SavedKg;
    }

    public void setTotalCO2SavedKg(double totalCO2SavedKg) {
        this.totalCO2SavedKg = totalCO2SavedKg;
    }

    public long getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(long totalSessions) {
        this.totalSessions = totalSessions;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalStations() {
        return totalStations;
    }

    public void setTotalStations(long totalStations) {
        this.totalStations = totalStations;
    }
}

}