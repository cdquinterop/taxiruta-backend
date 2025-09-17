---
mode: agent
model: claude Sonnet 4
---
#Mantener la estructura de carpetas sugerida
Dentro de src/main/java/com/slsolution/taxiruta/ crea los siguientes paquetes obligatorios:

├── src/main/java/com/yourcompany/taxiroute/
│   ├── TaxirouteApplication.java   // Main Spring Boot application class
│   │
│   ├── config/                     // General configurations
│   │   ├── SecurityConfig.java     // JWT and role-based security
│   │   ├── SwaggerConfig.java      // API documentation (optional)
│   │   └── WebConfig.java          // CORS, global mappings
│   │
│   ├── controller/                 // REST endpoints
│   │   ├── AuthController.java
│   │   ├── UserController.java
│   │   ├── TripController.java
│   │   └── BookingController.java
│   │
│   ├── service/                    // Business logic
│   │   ├── AuthService.java
│   │   ├── UserService.java
│   │   ├── TripService.java
│   │   └── BookingService.java
│   │
│   ├── repository/                 // Database access
│   │   ├── UserRepository.java
│   │   ├── TripRepository.java
│   │   └── BookingRepository.java
│   │
│   ├── model/                      // JPA entities
│   │   ├── User.java
│   │   ├── Trip.java
│   │   └── Booking.java
│   │
│   ├── dto/                        // Data Transfer Objects
│   │   ├── request/
│   │   │   ├── UserRegisterRequestDTO.java
│   │   │   ├── LoginRequestDTO.java
│   │   │   ├── TripRequestDTO.java
│   │   │   └── BookingRequestDTO.java
│   │   │
│   │   └── response/
│   │       ├── UserResponseDTO.java
│   │       ├── AuthResponseDTO.java
│   │       ├── TripResponseDTO.java
│   │       └── BookingResponseDTO.java
│   │
│   ├── exception/                  // Custom error handling
│   │   ├── ResourceNotFoundException.java
│   │   ├── BadRequestException.java
│   │   ├── UnauthorizedException.java
│   │   └── ApiExceptionHandler.java
│   │
│   └── util/                       // Utilities
│       ├── JwtUtil.java
│       └── Constants.java
│
├── src/main/resources/
│   ├── application.properties      // Spring Boot configuration (DB, JWT, etc.)
│   ├── data.sql                    // Optional: initial data for testing
│   ├── schema.sql                  // Optional: initial schema
│   └── db/migration/               // Flyway migration scripts
│       ├── V1__Create_users_table.sql
│       ├── V2__Create_trips_table.sql
│

##Tips para mantener la estructura consistente

Siempre crea nuevas funcionalidades dentro del paquete correspondiente:

Nuevos controladores → controller/

Nuevos servicios → service/

Nuevos modelos → model/

Evita mezclar lógica de negocio en los controladores.

Mantén DTOs y utilidades separados (dto/, util/).

Configuración general (SecurityConfig, WebConfig) en config/.

Las excepciones personalizadas en exception/.