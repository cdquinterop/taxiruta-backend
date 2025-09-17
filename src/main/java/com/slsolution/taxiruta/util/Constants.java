package com.slsolution.taxiruta.util;

public class Constants {

    // API Endpoints
    public static final String API_VERSION = "/api/v1";
    public static final String AUTH_ENDPOINT = API_VERSION + "/auth";
    public static final String USERS_ENDPOINT = API_VERSION + "/users";
    public static final String TRIPS_ENDPOINT = API_VERSION + "/trips";
    public static final String BOOKINGS_ENDPOINT = API_VERSION + "/bookings";

    // JWT Constants
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER_STRING = "Authorization";

    // Default Values
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    // User Roles
    public static final String ROLE_DRIVER = "DRIVER";
    public static final String ROLE_PASSENGER = "PASSENGER";

    // Trip Status
    public static final String TRIP_STATUS_ACTIVE = "ACTIVE";
    public static final String TRIP_STATUS_CANCELLED = "CANCELLED";
    public static final String TRIP_STATUS_COMPLETED = "COMPLETED";

    // Booking Status
    public static final String BOOKING_STATUS_PENDING = "PENDING";
    public static final String BOOKING_STATUS_CONFIRMED = "CONFIRMED";
    public static final String BOOKING_STATUS_REJECTED = "REJECTED";
    public static final String BOOKING_STATUS_CANCELLED = "CANCELLED";

    // Error Messages
    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_TRIP_NOT_FOUND = "Trip not found";
    public static final String ERROR_BOOKING_NOT_FOUND = "Booking not found";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ERROR_USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String ERROR_EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String ERROR_INSUFFICIENT_SEATS = "Not enough seats available";
    public static final String ERROR_UNAUTHORIZED_ACCESS = "Unauthorized access";
    public static final String ERROR_INVALID_ROLE = "Invalid role";
    public static final String ERROR_TRIP_NOT_ACTIVE = "Trip is not active";
    public static final String ERROR_BOOKING_ALREADY_EXISTS = "Booking already exists for this trip";

    // Success Messages
    public static final String SUCCESS_USER_REGISTERED = "User registered successfully";
    public static final String SUCCESS_LOGIN = "Login successful";
    public static final String SUCCESS_TRIP_CREATED = "Trip created successfully";
    public static final String SUCCESS_TRIP_UPDATED = "Trip updated successfully";
    public static final String SUCCESS_TRIP_CANCELLED = "Trip cancelled successfully";
    public static final String SUCCESS_TRIP_DELETED = "Trip deleted successfully";
    public static final String SUCCESS_BOOKING_CREATED = "Booking created successfully";
    public static final String SUCCESS_BOOKING_CONFIRMED = "Booking confirmed successfully";
    public static final String SUCCESS_BOOKING_REJECTED = "Booking rejected successfully";
    public static final String SUCCESS_BOOKING_CANCELLED = "Booking cancelled successfully";

    // Private constructor to prevent instantiation
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
}