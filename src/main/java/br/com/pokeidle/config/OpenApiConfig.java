package br.com.pokeidle.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI pokeidleOpenApi() {
        return new OpenAPI().info(new Info()
                .title("PokeIdle API")
                .version("v0")
                .description("API REST do backend modular do PokeIdle."));
    }
}
