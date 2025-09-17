-- V1__Create_initial_tables.sql

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(20) NOT NULL CHECK (role IN ('DRIVER', 'PASSENGER')),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create trips table
CREATE TABLE trips (
    id BIGSERIAL PRIMARY KEY,
    driver_id BIGINT NOT NULL,
    origin VARCHAR(200) NOT NULL,
    destination VARCHAR(200) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    available_seats INTEGER NOT NULL CHECK (available_seats > 0),
    price_per_seat DECIMAL(10,2) NOT NULL CHECK (price_per_seat > 0),
    description VARCHAR(500),
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'CANCELLED', 'COMPLETED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (driver_id) REFERENCES users(id)
);

-- Create bookings table
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    passenger_id BIGINT NOT NULL,
    seats_requested INTEGER NOT NULL CHECK (seats_requested > 0),
    total_price DECIMAL(10,2) NOT NULL CHECK (total_price > 0),
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'REJECTED', 'CANCELLED')),
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmed_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (trip_id) REFERENCES trips(id),
    FOREIGN KEY (passenger_id) REFERENCES users(id),
    UNIQUE(trip_id, passenger_id)
);

-- Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

CREATE INDEX idx_trips_driver_id ON trips(driver_id);
CREATE INDEX idx_trips_status ON trips(status);
CREATE INDEX idx_trips_departure_time ON trips(departure_time);
CREATE INDEX idx_trips_origin ON trips(origin);
CREATE INDEX idx_trips_destination ON trips(destination);

CREATE INDEX idx_bookings_trip_id ON bookings(trip_id);
CREATE INDEX idx_bookings_passenger_id ON bookings(passenger_id);
CREATE INDEX idx_bookings_status ON bookings(status);

-- Update timestamp trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_trips_updated_at BEFORE UPDATE ON trips 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_bookings_updated_at BEFORE UPDATE ON bookings 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();