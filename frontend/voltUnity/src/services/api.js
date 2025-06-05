// src/services/api.js

import axios from 'axios';

// Ajustar a tua base URL conforme o backend (ex: http://localhost:8080)
const API_BASE_URL = 'http://localhost:8080';

// Axios instance (opcional)
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    }
});

// ---- API FUNCTIONS ----

// DASHBOARD

// GET /dashboard-metrics → KPIs (co2Saved, totalRevenue, totalEnergy)
export const getDashboardMetrics = async () => {
    const response = await api.get('/dashboard-metrics');
    return response.data;
};

// GET /bookings/count → total bookings
export const getTotalBookings = async () => {
    const response = await api.get('/bookings/count');
    return response.data;
};

// GET /slots/status-summary → estado dos slots
export const getSlotStatusSummary = async () => {
    const response = await api.get('/slots/status-summary');
    return response.data;
};

// GET /stations/occupancy → bookings por estação (se tiveres este endpoint)
export const getBookingsPerStation = async () => {
    const response = await api.get('/stations/occupancy');
    return response.data;
};

// GET /bookings/recent → últimos bookings
export const getRecentBookings = async () => {
    const response = await api.get('/bookings/recent');
    return response.data;
};

// -----------------------------------------------------------
// OUTROS (já estavam planeados):

// GET /stations
export const getStations = async () => {
    const response = await api.get('/stations');
    return response.data;
};

// GET /stations/{id}
export const getStation = async (stationId) => {
    const response = await api.get(`/stations/${stationId}`);
    return response.data;
};

// POST /bookings
export const createBooking = async (stationId, slotId, userData) => {
    const bookingData = {
        stationId,
        slotId,
        ...userData
    };
    const response = await api.post('/bookings', bookingData);
    return response.data;
};

// PUT /slots/{id}/status
export const updateSlotStatus = async (slotId, newStatus) => {
    const response = await api.put(`/slots/${slotId}/status`, {
        slotStatus: newStatus
    });
    return response.data;
};