package com.voltunity.evplatform.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookingStatus;
    private LocalDateTime start;
    private LocalDateTime end;
    private float priceAtBooking;
    @ManyToOne
    private User user;
    @ManyToOne
    private ChargerPoint chargerPoint;

    // Construtor padrão
    public Booking() {}
    // Construtor com parâmetros
    public Booking(String bookingStatus, LocalDateTime start, LocalDateTime end, float priceAtBooking, User user, ChargerPoint chargerPoint) {
        this.bookingStatus = bookingStatus;
        this.start = start;
        this.end = end;
        this.priceAtBooking = priceAtBooking;
        this.user = user;
        this.chargerPoint = chargerPoint;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }
    public float getPriceAtBooking() { return priceAtBooking; }
    public void setPriceAtBooking(float priceAtBooking) { this.priceAtBooking = priceAtBooking; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public ChargerPoint getChargerPoint() { return chargerPoint; }
    public void setChargerPoint(ChargerPoint chargerPoint) { this.chargerPoint = chargerPoint; }
}