package br.com.pokeidle.inventario.application.command;

import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.mundo.domain.NoJornada;
import br.com.pokeidle.mundo.domain.NoJornadaRepository;
import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
import br.com.pokeidle.shared.domain.BusinessException;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurarTimeHandler {

    private final JogadorRepository jogadorRepository;
    private final NoJornadaRepository noJornadaRepository;
    private final PokemonCapturadoRepository pokemonCapturadoRepository;

    public CurarTimeHandler(JogadorRepository jogadorRepository,
                            NoJornadaRepository noJornadaRepository,
                            PokemonCapturadoRepository pokemonCapturadoRepository) {
        this.jogadorRepository = jogadorRepository;
        this.noJornadaRepository = noJornadaRepository;
        this.pokemonCapturadoRepository = pokemonCapturadoRepository;
    }

    @Transactional
    public void handle(CurarTimeCommand command) {
        Jogador jogador = jogadorRepository.findById(command.jogadorId())
                .orElseThrow(() -> new NotFoundException("Jogador nao encontrado."));
        NoJornada cidadeAtual = noJornadaRepository.findById(jogador.getNoAtualId())
                .orElseThrow(() -> new NotFoundException("No atual nao encontrado."));
        if (!cidadeAtual.ehCidade()) {
            throw new BusinessException("O time so pode ser curado em cidade.");
        }
        pokemonCapturadoRepository.findByJogadorIdOrderByCapturadoEmAsc(jogador.getId())
                .forEach(pokemon -> {
                    pokemon.curarTotalmente();
                    pokemonCapturadoRepository.save(pokemon);
                });
    }
}
