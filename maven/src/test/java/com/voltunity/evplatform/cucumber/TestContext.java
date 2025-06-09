package com.voltunity.evplatform.cucumber;

import org.springframework.stereotype.Component;

import com.voltunity.evplatform.model.Booking;

@Component
public class TestContext {
    public Long userId;
    public Long stationId;
    public double paymentAmount;
    public Booking booking;
}