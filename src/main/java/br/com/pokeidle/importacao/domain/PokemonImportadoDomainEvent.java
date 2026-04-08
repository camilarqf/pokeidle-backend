package br.com.pokeidle.importacao.domain;

import br.com.pokeidle.shared.domain.DomainEvent;

import java.time.Instant;

public record PokemonImportadoDomainEvent(Long pokemonEspecieId,
                                          String nome,
                                          Instant occurredAt) implements DomainEvent {

    public PokemonImportadoDomainEvent(Long pokemonEspecieId, String nome) {
        this(pokemonEspecieId, nome, Instant.now());
    }
}
