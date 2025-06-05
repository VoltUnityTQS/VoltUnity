-- USERS
INSERT INTO users (id, name, email, password, role) VALUES
(1, 'Alice', 'alice@example.com', 'pass123', 'USER'),
(2, 'Bob', 'bob@example.com', 'pass456', 'USER'),
(3, 'Charlie', 'charlie@example.com', 'pass789', 'ADMIN'),
(4, 'Diana', 'diana@example.com', 'pass321', 'USER');

-- STATIONS
INSERT INTO station (id, name, station_status, lat, lng, address, total_slots, max_power, price_per_kwh) VALUES
(1, 'Station A', 'ACTIVE', 40.0, -8.0, 'Rua A', 4, 22.0, 0.30),
(2, 'Station B', 'MAINTENANCE', 41.0, -8.5, 'Rua B', 2, 50.0, 0.45),
(3, 'Station C', 'ACTIVE', 39.5, -7.9, 'Rua C', 3, 11.0, 0.25);

-- SLOTS
INSERT INTO slot (id, slot_status, power, station_id) VALUES
(1, 'AVAILABLE', 22.0, 1),
(2, 'IN_USE', 22.0, 1),
(3, 'MAINTENANCE', 22.0, 1),
(4, 'AVAILABLE', 50.0, 2),
(5, 'AVAILABLE', 11.0, 3);

-- SUBSCRIPTIONS
INSERT INTO subscription (id, subscription_type, start_date, end_date, price_per_month, sessions_included, discount_rate, user_id) VALUES
(1, 'BASIC', NOW() - INTERVAL '1 month', NOW() + INTERVAL '11 months', 10.0, 5, 0.05, 1),
(2, 'PREMIUM', NOW() - INTERVAL '2 months', NOW() + INTERVAL '10 months', 20.0, 10, 0.10, 2);

-- BOOKINGS
INSERT INTO booking (id, booking_status, start, "end", price_at_booking, user_id, slot_id) VALUES
(1, 'confirmed', NOW(), NOW() + INTERVAL '1 hour', 5.0, 1, 1),
(2, 'cancelled', NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day', 7.5, 2, 2);

-- CHARGING_SESSIONS
INSERT INTO charging_session (id, start_timestamp, end_timestamp, energy_kwh, total_price, user_id, slot_id) VALUES
(1, NOW() - INTERVAL '1 day', NOW() - INTERVAL '23 hours', 15.0, 4.5, 1, 1),
(2, NOW() - INTERVAL '5 days', NOW() - INTERVAL '4 days 20 hours', 10.0, 3.0, 2, 2);

-- PAYMENTS
INSERT INTO payment (id, payment_status, amount, currency, timestamp, user_id) VALUES
(1, 'COMPLETED', 25.0, 'EUR', NOW() - INTERVAL '1 week', 1),
(2, 'COMPLETED', 50.0, 'EUR', NOW() - INTERVAL '2 weeks', 2);