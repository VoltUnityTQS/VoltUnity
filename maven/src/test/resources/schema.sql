-- USERS
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100),
                       email VARCHAR(100),
                       password VARCHAR(100),
                       role VARCHAR(20)
);

-- CARS
CREATE TABLE car (
                     id SERIAL PRIMARY KEY,
                     make VARCHAR(50),
                     model VARCHAR(50),
                     license_plate VARCHAR(20),
                     user_id INT REFERENCES users(id)
);

-- STATIONS
CREATE TABLE station (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100),
                         station_status VARCHAR(20),
                         lat DOUBLE PRECISION,
                         lng DOUBLE PRECISION,
                         address VARCHAR(200),
                         total_slots INT,
                         max_power DOUBLE PRECISION,
                         price_per_kwh DOUBLE PRECISION
);

-- SLOTS
CREATE TABLE slot (
                      id SERIAL PRIMARY KEY,
                      slot_status VARCHAR(20),
                      power DOUBLE PRECISION,
                      station_id INT REFERENCES station(id)
);

-- SUBSCRIPTIONS
CREATE TABLE subscription (
                              id SERIAL PRIMARY KEY,
                              subscription_type VARCHAR(50),
                              start_date TIMESTAMP,
                              end_date TIMESTAMP,
                              price_per_month DOUBLE PRECISION,
                              sessions_included INT,
                              discount_rate DOUBLE PRECISION,
                              user_id INT REFERENCES users(id)
);

-- BOOKINGS
CREATE TABLE booking (
                         id SERIAL PRIMARY KEY,
                         booking_status VARCHAR(20),
                         start TIMESTAMP,
                         end TIMESTAMP,
                         price_at_booking DOUBLE PRECISION,
                         user_id INT REFERENCES users(id),
                         slot_id INT REFERENCES slot(id)
);

-- CHARGING_SESSIONS
CREATE TABLE charging_session (
                                  id SERIAL PRIMARY KEY,
                                  start_timestamp TIMESTAMP,
                                  end_timestamp TIMESTAMP,
                                  energy_kwh DOUBLE PRECISION,
                                  total_price DOUBLE PRECISION,
                                  user_id INT REFERENCES users(id),
                                  slot_id INT REFERENCES slot(id)
);

-- PAYMENTS
CREATE TABLE payment (
                         id SERIAL PRIMARY KEY,
                         payment_status VARCHAR(20),
                         amount DOUBLE PRECISION,
                         currency VARCHAR(10),
                         timestamp TIMESTAMP,
                         user_id INT REFERENCES users(id)
);