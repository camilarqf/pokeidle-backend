package br.com.pokeidle.progresso.domain;

import br.com.pokeidle.shared.domain.DomainEvent;

import java.time.Instant;

public record MissaoDeNoConcluidaDomainEvent(String jogadorId,
                                             Long noJornadaId,
                                             Instant occurredAt) implements DomainEvent {

    public MissaoDeNoConcluidaDomainEvent(String jogadorId, Long noJornadaId) {
        this(jogadorId, noJornadaId, Instant.now());
    }
}
