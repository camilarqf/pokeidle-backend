package br.com.pokeidle.shared.infrastructure;

import br.com.pokeidle.shared.application.DomainEventPublisher;
import br.com.pokeidle.shared.domain.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishAll(Collection<? extends DomainEvent> events) {
        events.forEach(applicationEventPublisher::publishEvent);
    }
}
