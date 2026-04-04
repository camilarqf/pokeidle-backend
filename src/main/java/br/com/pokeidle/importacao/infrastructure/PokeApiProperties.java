package br.com.pokeidle.importacao.infrastructure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pokeapi")
public record PokeApiProperties(String baseUrl) {
}
