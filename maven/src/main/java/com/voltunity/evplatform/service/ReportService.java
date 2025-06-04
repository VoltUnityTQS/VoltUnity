package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.ChargingSession;
import com.voltunity.evplatform.repository.ChargingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
}