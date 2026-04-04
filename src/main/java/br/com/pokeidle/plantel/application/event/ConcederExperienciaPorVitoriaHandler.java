package br.com.pokeidle.plantel.application.event;

import br.com.pokeidle.batalha.domain.BatalhaSelvagemVencidaDomainEvent;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
import br.com.pokeidle.shared.domain.NotFoundException;
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
        PokemonCapturado pokemon = pokemonCapturadoRepository.findById(event.pokemonJogadorId())
                .orElseThrow(() -> new NotFoundException("Pokemon do jogador nao encontrado."));
        pokemon.ganharExperiencia(event.experienciaRecebida());
        pokemonCapturadoRepository.save(pokemon);
    }
}
