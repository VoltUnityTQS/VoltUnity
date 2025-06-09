package com.voltunity.evplatform.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stations")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String stationStatus;
    private float lat;
    private float lng;
    private String address;
    private int totalSlots;
    private float maxPower;
    private double pricePerKWh;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> chargerTypes;

    public Station() {}

    public Station(String name, String stationStatus, float lat, float lng, String address, int totalSlots, float maxPower, List<String> chargerTypes) {
        this.name = name;
        this.stationStatus = stationStatus;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.totalSlots = totalSlots;
        this.maxPower = maxPower;
        this.chargerTypes = chargerTypes;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(String stationStatus) {
        this.stationStatus = stationStatus;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public float getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(float maxPower) {
        this.maxPower = maxPower;
    }

    public List<String> getChargerTypes() {
        return chargerTypes;
    }

    public void setChargerTypes(List<String> chargerTypes) {
        this.chargerTypes = chargerTypes;
    }

    public double getPricePerKWh() {
        return pricePerKWh;
    }

    public void setPricePerKWh(double pricePerKWh) {
        this.pricePerKWh = pricePerKWh;
    }
}