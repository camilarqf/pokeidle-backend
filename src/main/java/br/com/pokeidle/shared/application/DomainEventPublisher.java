package br.com.pokeidle.shared.application;

import br.com.pokeidle.shared.domain.DomainEvent;

import java.util.Collection;

public interface DomainEventPublisher {

    void publishAll(Collection<? extends DomainEvent> events);
}
