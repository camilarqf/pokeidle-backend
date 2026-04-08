package br.com.pokeidle.treinadores.domain;

import br.com.pokeidle.shared.domain.DomainEvent;

import java.time.Instant;

public record TreinadorNpcDerrotadoDomainEvent(String jogadorId,
                                               Long treinadorNpcId,
                                               Long noJornadaId,
                                               int recompensaMoedas,
                                               int experienciaRecebida,
                                               String pokemonJogadorId,
                                               Instant occurredAt) implements DomainEvent {

    public TreinadorNpcDerrotadoDomainEvent(String jogadorId,
                                            Long treinadorNpcId,
                                            Long noJornadaId,
                                            int recompensaMoedas,
                                            int experienciaRecebida,
                                            String pokemonJogadorId) {
        this(jogadorId, treinadorNpcId, noJornadaId, recompensaMoedas, experienciaRecebida, pokemonJogadorId, Instant.now());
    }
}
