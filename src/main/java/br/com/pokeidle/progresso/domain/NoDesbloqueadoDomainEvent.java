package br.com.pokeidle.progresso.domain;

import br.com.pokeidle.shared.domain.DomainEvent;

import java.time.Instant;

public record NoDesbloqueadoDomainEvent(String jogadorId, Long noId, Instant occurredAt) implements DomainEvent {

    public NoDesbloqueadoDomainEvent(String jogadorId, Long noId) {
        this(jogadorId, noId, Instant.now());
    }
}
