---
mode: agent
model: claude Sonnet 4
---
Provide instructions for implementing all REST endpoints for the TaxiRuta Backend MVP project.

User / Auth

POST /api/auth/register → Registro usuario

POST /api/auth/login → Login JWT

GET /api/users/{id} → Perfil usuario

PUT /api/users/{id} → Actualizar perfil

Trip

POST /api/trips → Crear viaje (conductor)

GET /api/trips → Listar viajes filtrados (origin/destination/date)

GET /api/trips/{id} → Detalle de viaje

PUT /api/trips/{id} → Actualizar viaje

DELETE /api/trips/{id} → Cancelar viaje

Booking / Reservation

POST /api/trips/{tripId}/bookings → Solicitar asiento (pasajero)

PUT /api/trips/{tripId}/bookings/{bookingId}/accept → Aceptar reserva (conductor)

PUT /api/trips/{tripId}/bookings/{bookingId}/reject → Rechazar reserva (conductor)

GET /api/trips/{tripId}/bookings → Lista de pasajeros (conductor)

GET /api/users/{userId}/bookings → Reservas del pasajero

DELETE /api/trips/{tripId}/bookings/{bookingId} → Cancelar reserva