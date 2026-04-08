package br.com.pokeidle.treinadores.domain;

import br.com.pokeidle.shared.domain.DomainEvent;

import java.time.Instant;

public record LiderDeGinasioDerrotadoDomainEvent(String jogadorId,
                                                 Long treinadorNpcId,
                                                 Long ginasioId,
                                                 Long noJornadaId,
                                                 int recompensaMoedas,
                                                 int experienciaRecebida,
                                                 String pokemonJogadorId,
                                                 Instant occurredAt) implements DomainEvent {

    public LiderDeGinasioDerrotadoDomainEvent(String jogadorId,
                                              Long treinadorNpcId,
                                              Long ginasioId,
                                              Long noJornadaId,
                                              int recompensaMoedas,
                                              int experienciaRecebida,
                                              String pokemonJogadorId) {
        this(jogadorId, treinadorNpcId, ginasioId, noJornadaId, recompensaMoedas, experienciaRecebida, pokemonJogadorId, Instant.now());
    }
}
