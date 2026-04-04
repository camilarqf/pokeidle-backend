package br.com.pokeidle.jogador.domain;

import br.com.pokeidle.shared.domain.DomainEvent;

import java.time.Instant;

public record PokemonInicialEscolhidoDomainEvent(String jogadorId,
                                                 String pokemonCapturadoId,
                                                 Instant occurredAt) implements DomainEvent {

    public PokemonInicialEscolhidoDomainEvent(String jogadorId, String pokemonCapturadoId) {
        this(jogadorId, pokemonCapturadoId, Instant.now());
    }
}
