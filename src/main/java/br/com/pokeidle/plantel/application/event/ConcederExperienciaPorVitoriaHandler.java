package br.com.pokeidle.plantel.application.event;

import br.com.pokeidle.batalha.domain.BatalhaSelvagemVencidaDomainEvent;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
import br.com.pokeidle.shared.domain.NotFoundException;
import br.com.pokeidle.treinadores.domain.LiderDeGinasioDerrotadoDomainEvent;
import br.com.pokeidle.treinadores.domain.TreinadorNpcDerrotadoDomainEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConcederExperienciaPorVitoriaHandler {

    private final PokemonCapturadoRepository pokemonCapturadoRepository;

    public ConcederExperienciaPorVitoriaHandler(PokemonCapturadoRepository pokemonCapturadoRepository) {
        this.pokemonCapturadoRepository = pokemonCapturadoRepository;
    }

    @EventListener
    @Transactional
    public void on(BatalhaSelvagemVencidaDomainEvent event) {
        conceder(event.pokemonJogadorId(), event.experienciaRecebida());
    }

    @EventListener
    @Transactional
    public void on(TreinadorNpcDerrotadoDomainEvent event) {
        conceder(event.pokemonJogadorId(), event.experienciaRecebida());
    }

    @EventListener
    @Transactional
    public void on(LiderDeGinasioDerrotadoDomainEvent event) {
        conceder(event.pokemonJogadorId(), event.experienciaRecebida());
    }

    private void conceder(String pokemonJogadorId, int experienciaRecebida) {
        PokemonCapturado pokemon = pokemonCapturadoRepository.findById(pokemonJogadorId)
                .orElseThrow(() -> new NotFoundException("Pokemon do jogador nao encontrado."));
        pokemon.ganharExperiencia(experienciaRecebida);
        pokemonCapturadoRepository.save(pokemon);
    }
}
