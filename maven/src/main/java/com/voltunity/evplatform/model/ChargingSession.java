package com.voltunity.evplatform.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ChargingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String sessionStatus; // "ACTIVE", "COMPLETED", "CANCELLED"

    private LocalDateTime startTimestamp;

    private LocalDateTime endTimestamp;

    private double energyConsumedKWh; // em kWh

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public LocalDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(LocalDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public LocalDateTime getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(LocalDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public double getEnergyConsumedKWh() {
        return energyConsumedKWh;
    }

    public void setEnergyConsumedKWh(double energyConsumedKWh) {
        this.energyConsumedKWh = energyConsumedKWh;
    }
}