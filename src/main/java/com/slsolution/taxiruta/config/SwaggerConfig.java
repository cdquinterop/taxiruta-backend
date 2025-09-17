package com.slsolution.taxiruta.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración profesional de Swagger/OpenAPI para documentación de la API
 * Implementado siguiendo las mejores prácticas de documentación de APIs REST
 * @author SL Solution Team
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TaxiRuta Backend API")
                        .description("API REST empresarial para el sistema de compartición de viajes TaxiRuta. " +
                                "Permite a conductores ofrecer viajes programados y a pasajeros reservar asientos " +
                                "de manera segura y eficiente. Implementa autenticación JWT y validaciones robustas.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SL Solution Development Team")
                                .email("dev@slsolution.com")
                                .url("https://www.slsolution.com"))
                        .license(new License()
                                .name("Proprietary License")
                                .url("https://www.slsolution.com/license")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Entorno de Desarrollo"),
                        new Server()
                                .url("https://staging-api.taxiruta.com")
                                .description("Entorno de Staging"),
                        new Server()
                                .url("https://api.taxiruta.com")
                                .description("Entorno de Producción")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT para autenticación. " +
                                                "Formato: 'Bearer {token}'. " +
                                                "Obtén el token mediante el endpoint /api/auth/login")));
    }
}