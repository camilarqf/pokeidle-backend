package br.com.pokeidle.captura.domain;

import br.com.pokeidle.shared.domain.DomainEvent;

import java.time.Instant;

public record PokemonCapturadoDomainEvent(String batalhaId,
                                          String jogadorId,
                                          Long especieId,
                                          String pokemonCapturadoId,
                                          Instant occurredAt) implements DomainEvent {

    public PokemonCapturadoDomainEvent(String batalhaId, String jogadorId, Long especieId, String pokemonCapturadoId) {
        this(batalhaId, jogadorId, especieId, pokemonCapturadoId, Instant.now());
    }
}
