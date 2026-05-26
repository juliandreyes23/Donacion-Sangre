package co.sena.cimm.adso.Sistema_Donacion_Sangre.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Donación de Sangre API")
                        .description("API REST para gestionar el proceso de donación de sangre: " +
                                "donantes, donaciones, consentimientos e inventario.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("SENA - CIMM ADSO")
                                .email("adso@sena.edu.co"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))

                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))

                .schemaRequirement(securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));
    }
}