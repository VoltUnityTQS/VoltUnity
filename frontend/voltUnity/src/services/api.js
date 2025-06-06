// src/services/api.js

import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1';

export const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

export const getStations = async () => {
    const response = await api.get('/stations');
    return response.data;
};

export const getStation = async (stationId) => {
    const response = await api.get(`/stations/${stationId}`);
    return response.data;
};

export const createBooking = async (stationId, slotId, userData) => {
    const bookingData = {
        stationId,
        slotId,
        ...userData
    };
    const response = await api.post('/bookings', bookingData);
    return response.data;
};

export const updateSlotStatus = async (slotId, newStatus) => {
    const response = await api.put(`/slots/${slotId}/status`, {
        slotStatus: newStatus
    });
    return response.data;
};