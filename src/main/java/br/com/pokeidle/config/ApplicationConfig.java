package br.com.pokeidle.config;

import br.com.pokeidle.shared.domain.RandomProvider;
import br.com.pokeidle.shared.infrastructure.DefaultRandomProvider;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ApplicationConfig {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Bean
    RandomProvider randomProvider() {
        return new DefaultRandomProvider();
    }
}
