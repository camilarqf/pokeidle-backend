package br.com.pokeidle.batalha.domain;

import br.com.pokeidle.shared.domain.DomainEvent;

import java.time.Instant;

public record BatalhaSelvagemVencidaDomainEvent(String batalhaId,
                                                String jogadorId,
                                                Long noId,
                                                String pokemonJogadorId,
                                                int experienciaRecebida,
                                                Instant occurredAt) implements DomainEvent {

    public BatalhaSelvagemVencidaDomainEvent(String batalhaId,
                                             String jogadorId,
                                             Long noId,
                                             String pokemonJogadorId,
                                             int experienciaRecebida) {
        this(batalhaId, jogadorId, noId, pokemonJogadorId, experienciaRecebida, Instant.now());
    }
}
