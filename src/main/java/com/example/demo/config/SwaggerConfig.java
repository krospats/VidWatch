package com.example.demo.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Video Service API")
                        .version("1.0")
                        .description("API для управления видео и пользователями"))
                .externalDocs(new ExternalDocumentation()
                        .description("Дополнительная документация")
                        .url("https://example.com/docs"));
    }
}

