package com.safiap.techchallengeoficinamecanica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Workshop API")
                        .description("API for integrated mechanical workshop management")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tech Challenge Team")
                                .email("techchalengerteam@email.com")))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token - obtain at endpoint /api/v1/auth/login")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
}