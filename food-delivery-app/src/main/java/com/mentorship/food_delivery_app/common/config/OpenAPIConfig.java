package com.mentorship.food_delivery_app.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Food Delivery Backend Platform"
                ),
                description = "OpenAPI Doc for spring Food Delivery project",
                title = "OpenAPI Specification",
                version = "1.0"
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(name = "bearerAuth",
        description = "JWT auth description",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer"
)
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenApiCustomizer customizer(){
        return openApi ->
                openApi.getPaths().forEach((path, pathItem)->{
                    if (path.contains("public"))
                        pathItem.readOperations().
                                forEach(operation->operation.security(Collections.emptyList()));
                });
    }
}
