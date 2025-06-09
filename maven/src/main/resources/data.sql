-- USERS
INSERT INTO users (id, name, email, password, role) VALUES
(1, 'Alice', 'alice@example.com', 'pass123', 'USER'),
(2, 'Bob', 'bob@example.com', 'pass456', 'USER'),
(3, 'Charlie', 'charlie@example.com', 'pass789', 'ADMIN'),
(4, 'Diana', 'diana@example.com', 'pass321', 'USER');

-- STATIONS
INSERT INTO stations (id, name, station_status, lat, lng, address, total_slots, max_power, price_perkwh) VALUES
(1, 'Station A', 'ACTIVE', 40.0, -8.0, 'Rua A', 4, 22.0, 0.30),
(2, 'Station B', 'MAINTENANCE', 41.0, -8.5, 'Rua B', 2, 50.0, 0.45),
(3, 'Station C', 'ACTIVE', 39.5, -7.9, 'Rua C', 3, 11.0, 0.25),
(4, 'Station D', 'ACTIVE', 38.7, -9.1, 'Rua D', 5, 22.0, 0.40);

-- SLOTS
INSERT INTO slots (id, slot_status, power, slot_number, type, station_id) VALUES
(1, 'AVAILABLE', 22.0, 1, 'AC', 1),
(2, 'IN_USE', 22.0, 2, 'AC', 1),
(3, 'MAINTENANCE', 22.0, 3, 'AC', 1),
(4, 'AVAILABLE', 50.0, 1, 'DC', 2),
(5, 'AVAILABLE', 11.0, 1, 'AC', 3),
(6, 'IN_USE', 22.0, 1, 'AC', 4),
(7, 'AVAILABLE', 22.0, 2, 'AC', 4),
(8, 'MAINTENANCE', 22.0, 3, 'AC', 4),
(9, 'AVAILABLE', 22.0, 4, 'DC', 4),
(10, 'AVAILABLE', 22.0, 5, 'DC', 4);

-- STATION_CHARGER_TYPES
INSERT INTO station_charger_types (station_id, charger_types) VALUES
(1, 'AC'),
(1, 'DC'),
(2, 'DC'),
(3, 'AC'),
(4, 'AC'),
(4, 'DC');

-- SUBSCRIPTIONS
INSERT INTO subscriptions (id, subscription_type, start_date, end_date, price_per_month, sessions_included, discount_perkwh, status, user_id) VALUES
(1, 'BASIC', NOW() - INTERVAL '1 month', NOW() + INTERVAL '11 months', 10.0, 5, 0.05, 'ACTIVE', 1),
(2, 'PREMIUM', NOW() - INTERVAL '2 months', NOW() + INTERVAL '10 months', 20.0, 10, 0.10, 'ACTIVE', 2),
(3, 'BASIC', NOW() - INTERVAL '3 months', NOW() + INTERVAL '9 months', 10.0, 5, 0.05, 'ACTIVE', 3);

-- BOOKINGS
INSERT INTO bookings (booking_status, start, end_timestamp, price_at_booking, user_id, slot_id) VALUES
('CONFIRMED', NOW(), NOW() + INTERVAL '1 hour', 5.0, 1, 1),
('CANCELLED', NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day', 7.5, 2, 2),
('CONFIRMED', NOW() - INTERVAL '3 days', NOW() - INTERVAL '2 days', 4.0, 3, 4),
('CONFIRMED', NOW() - INTERVAL '10 days', NOW() - INTERVAL '9 days', 6.5, 1, 5),
('CONFIRMED', NOW() - INTERVAL '5 hours', NOW() - INTERVAL '3 hours', 8.0, 4, 6),
('CANCELLED', NOW() - INTERVAL '7 days', NOW() - INTERVAL '6 days', 10.0, 2, 7);

-- CHARGING_SESSIONS
INSERT INTO charging_sessions (id, start_timestamp, end_timestamp, energy_consumedkwh, session_status, user_id, slot_id) VALUES
(1, NOW() - INTERVAL '1 day', NOW() - INTERVAL '23 hours', 15.0, 'COMPLETED', 1, 1),
(2, NOW() - INTERVAL '5 days', NOW() - INTERVAL '4 days 20 hours', 10.0, 'COMPLETED', 2, 2),
(3, NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day 20 hours', 20.0, 'COMPLETED', 3, 4),
(4, NOW() - INTERVAL '3 hours', NOW() - INTERVAL '2 hours', 7.5, 'COMPLETED', 1, 6),
(5, NOW() - INTERVAL '8 days', NOW() - INTERVAL '7 days 22 hours', 12.0, 'COMPLETED', 4, 5);

-- PAYMENTS
INSERT INTO payments (id, amount, currency, payment_status, timestamp, user_id) VALUES
(1, 25.0, 'EUR', 'COMPLETED', NOW() - INTERVAL '1 week', 1),
(2, 50.0, 'EUR', 'COMPLETED', NOW() - INTERVAL '2 weeks', 2),
(3, 35.0, 'EUR', 'COMPLETED', NOW() - INTERVAL '3 days', 3),
(4, 15.0, 'EUR', 'PENDING', NOW() - INTERVAL '1 day', 4),
(5, 45.0, 'EUR', 'COMPLETED', NOW() - INTERVAL '4 days', 1);

-- CARS
INSERT INTO cars (id, make, model, license_plate, user_id) VALUES
(1, 'Tesla', 'Model 3', '00-AA-00', 1),
(2, 'Nissan', 'Leaf', '11-BB-11', 2),
(3, 'Renault', 'Zoe', '22-CC-22', 3),
(4, 'BMW', 'i3', '33-DD-33', 4),
(5, 'Hyundai', 'Kona', '44-EE-44', 1),
(6, 'Peugeot', 'e-208', '55-FF-55', 2);

-- Reset car sequence
SELECT setval('cars_id_seq', (SELECT MAX(id) FROM cars));