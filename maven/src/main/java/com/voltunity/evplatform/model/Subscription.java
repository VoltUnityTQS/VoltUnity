package com.voltunity.evplatform.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String subscriptionType; // "FREE", "BASIC", "PREMIUM", "ENTERPRISE"

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String status; // "ACTIVE", "CANCELLED", "EXPIRED"

    private double pricePerMonth; // em EUR

    private int sessionsIncluded; // nº de sessões incluídas / mês

    private double discountPerKWh; // em %

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPricePerMonth() {
        return pricePerMonth;
    }

    public void setPricePerMonth(double pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public int getSessionsIncluded() {
        return sessionsIncluded;
    }

    public void setSessionsIncluded(int sessionsIncluded) {
        this.sessionsIncluded = sessionsIncluded;
    }

    public double getDiscountPerKWh() {
        return discountPerKWh;
    }

    public void setDiscountPerKWh(double discountPerKWh) {
        this.discountPerKWh = discountPerKWh;
    }
}