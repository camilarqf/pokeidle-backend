package br.com.pokeidle.jogador.domain;

import br.com.pokeidle.shared.domain.DomainEvent;

import java.time.Instant;

public record LevelCapAumentadoDomainEvent(String jogadorId,
                                           int novoLevelCap,
                                           Instant occurredAt) implements DomainEvent {

    public LevelCapAumentadoDomainEvent(String jogadorId, int novoLevelCap) {
        this(jogadorId, novoLevelCap, Instant.now());
    }
}
