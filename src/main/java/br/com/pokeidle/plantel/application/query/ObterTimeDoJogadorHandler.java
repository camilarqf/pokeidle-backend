package br.com.pokeidle.plantel.application.query;

import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObterTimeDoJogadorHandler {

    private final PokemonCapturadoRepository pokemonCapturadoRepository;

    public ObterTimeDoJogadorHandler(PokemonCapturadoRepository pokemonCapturadoRepository) {
        this.pokemonCapturadoRepository = pokemonCapturadoRepository;
    }

    public List<PokemonDoTimeDto> handle(String jogadorId) {
        return pokemonCapturadoRepository.findByJogadorIdOrderByCapturadoEmAsc(jogadorId).stream()
                .map(pokemon -> new PokemonDoTimeDto(
                        pokemon.getId(),
                        pokemon.getEspecieId(),
                        pokemon.getNome(),
                        pokemon.getNivel(),
                        pokemon.getExperiencia(),
                        pokemon.getHpAtual(),
                        pokemon.getHpMaximo(),
                        pokemon.getAtaque(),
                        pokemon.getDefesa(),
                        pokemon.getVelocidade(),
                        pokemon.isInicial(),
                        pokemon.isAtivo()
                ))
                .toList();
    }
}
