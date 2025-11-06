package com.zaid.transaction.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Banking System API Documentation",
                version = "v1.0",
                description = "Dokumentasi REST API untuk Sistem Perbankan (Transfer, Deposit, History) yang dilindungi oleh JWT Security.",
                contact = @Contact(
                        name = "m.zaidanshori04@gmail.com",
                        email = "m.zaidanshori04@gmail.com"
                ),
                license = @License(
                        name = "github.com/zaidnshr1/Bank-with-JWT-Spring-Boot",
                        url = "https://github.com/zaidnshr1/Bank-with-JWT-Spring-Boot"
                )
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Masukkan Kode JWT"
)
public class OpenApiConfig {
}
