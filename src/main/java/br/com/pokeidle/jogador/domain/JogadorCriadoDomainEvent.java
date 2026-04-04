package br.com.pokeidle.jogador.domain;

import br.com.pokeidle.shared.domain.DomainEvent;

import java.time.Instant;

public record JogadorCriadoDomainEvent(String jogadorId, Instant occurredAt) implements DomainEvent {

    public JogadorCriadoDomainEvent(String jogadorId) {
        this(jogadorId, Instant.now());
    }
}
