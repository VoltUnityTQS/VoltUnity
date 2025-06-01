package com.voltunity.evplatform.model;

import jakarta.persistence.*;

@Entity
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int slotNumber;

    private String slotStatus; // AVAILABLE, IN_USE, MAINTENANCE

    private float power; // ex: 22.0 kW, 50.0 kW

    private String type; // Normal, Supercharger

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    // Construtor padrão
    public Slot() {}

    // Construtor com parâmetros
    public Slot(int slotNumber, String slotStatus, float power, String type, Station station) {
        this.slotNumber = slotNumber;
        this.slotStatus = slotStatus;
        this.power = power;
        this.type = type;
        this.station = station;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getSlotStatus() {
        return slotStatus;
    }

    public void setSlotStatus(String slotStatus) {
        this.slotStatus = slotStatus;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}