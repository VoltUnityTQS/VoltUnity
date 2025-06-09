// src/services/api.js

import axios from 'axios';

const API_BASE_URL = `${import.meta.env.VITE_API_BASE_URL}`;

export const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json'
    },
});

export const getStations = async () => {
    const response = await api.get('/stations');
    return response.data;
};


export const getStationSlots = async (stationId) => {
    const response = await api.get(`/stations/${stationId}/slots`);
    return response.data;
};

export const createBooking = async (bookingData) => {
    const response = await api.post('/reservations', bookingData);
    return response.data;
};

 export const getUsers = async () => {
    const response = await api.get('/users');
    return response.data;
};

export const updateSlotStatus = async (slotId, newStatus) => {
    const response = await api.put(`/slots/${slotId}/status`, {
        slotStatus: newStatus
    });
    return response.data;
};

export const getUserById = async (userId) => {
    const response = await api.get(`/users/${userId}`);
    return response.data;
};

export const getCarsByUser = async (userId) => {
    const response = await api.get(`/cars/users/${userId}`, {
        headers: {
            'X-User-Id': userId
        }
    });
    return response.data;
};

export const addCar = async (carData) => {
    const response = await api.post('/cars', carData);
    return response.data;
};

export const deleteCar = async (carId, userId) => {
    const response = await api.delete(`/cars/${carId}`, {
        headers: {
            'X-User-Id': userId
        }
    });
    return response.data;
};



export const addStation = async (stationData) => {
    const response = await api.post('/stations', stationData);
    return response.data;
};

// Slots
export const addSlot = async (stationId, slotData) => {
    const response = await api.post(`/stations/${stationId}/slots`, slotData);
    return response.data;
};

// Users


export const addUser = async (userData) => {
    const response = await api.post('/users', userData);
    return response.data;
};

export const deleteUser = async (userId) => {
    await api.delete(`/users/${userId}`);
};

// Cars
export const getAllCars = async () => {
    const response = await api.get('/cars');
    return response.data;
};

export const addCarGlobal = async (carData) => {
    const response = await api.post('/cars', carData);
    return response.data;
};

export const deleteCarGlobal = async (carId) => {
    await api.delete(`/cars/${carId}`);
};