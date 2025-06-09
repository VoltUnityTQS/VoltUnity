-- USERS
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100),
    role VARCHAR(20)
);

-- CARS
CREATE TABLE IF NOT EXISTS cars (
    id SERIAL PRIMARY KEY,
    make VARCHAR(50),
    model VARCHAR(50),
    license_plate VARCHAR(20) UNIQUE,
    user_id INT REFERENCES users(id)
);

-- STATIONS
CREATE TABLE IF NOT EXISTS stations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    station_status VARCHAR(20),
    lat DOUBLE PRECISION,
    lng DOUBLE PRECISION,
    address VARCHAR(200),
    total_slots INT,
    max_power DOUBLE PRECISION,
    price_perkwh DOUBLE PRECISION
);

-- SLOTS
CREATE TABLE IF NOT EXISTS slots (
    id SERIAL PRIMARY KEY,
    slot_status VARCHAR(20),
    power DOUBLE PRECISION,
    slot_number INT,
    type VARCHAR(50),
    station_id INT REFERENCES stations(id)
);

-- STATION_CHARGER_TYPES
CREATE TABLE IF NOT EXISTS station_charger_types (
    station_id INT REFERENCES stations(id),
    charger_types VARCHAR(255)
);

-- SUBSCRIPTIONS
CREATE TABLE IF NOT EXISTS subscriptions (
    id SERIAL PRIMARY KEY,
    subscription_type VARCHAR(50),
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    price_per_month DOUBLE PRECISION,
    sessions_included INT,
    discount_perkwh DOUBLE PRECISION,
    status VARCHAR(20),
    user_id INT REFERENCES users(id)
);

-- BOOKINGS
CREATE TABLE IF NOT EXISTS bookings (
    id SERIAL PRIMARY KEY,
    booking_status VARCHAR(20),
    start TIMESTAMP,
    end_timestamp TIMESTAMP,
    price_at_booking DOUBLE PRECISION,
    user_id INT REFERENCES users(id),
    slot_id INT REFERENCES slots(id)
);

-- CHARGINGSESSIONS
CREATE TABLE IF NOT EXISTS charging_sessions (
    id SERIAL PRIMARY KEY,
    start_timestamp TIMESTAMP,
    end_timestamp TIMESTAMP,
    energy_consumedkwh DOUBLE PRECISION,
    session_status VARCHAR(50),
    user_id INT REFERENCES users(id),
    slot_id INT REFERENCES slots(id)
);

-- PAYMENTS
CREATE TABLE IF NOT EXISTS payments (
    id SERIAL PRIMARY KEY,
    amount DOUBLE PRECISION,
    currency VARCHAR(10),
    payment_status VARCHAR(20),
    timestamp TIMESTAMP,
    user_id INT REFERENCES users(id)
);