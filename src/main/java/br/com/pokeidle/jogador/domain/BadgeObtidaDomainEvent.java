package br.com.pokeidle.jogador.domain;

import br.com.pokeidle.shared.domain.DomainEvent;

import java.time.Instant;

public record BadgeObtidaDomainEvent(String jogadorId,
                                     CodigoBadge codigo,
                                     String nomeBadge,
                                     Instant occurredAt) implements DomainEvent {

    public BadgeObtidaDomainEvent(String jogadorId, CodigoBadge codigo, String nomeBadge) {
        this(jogadorId, codigo, nomeBadge, Instant.now());
    }
}
