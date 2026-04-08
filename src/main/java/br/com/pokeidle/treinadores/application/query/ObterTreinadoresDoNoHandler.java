package br.com.pokeidle.treinadores.application.query;

import br.com.pokeidle.mundo.domain.NoJornadaRepository;
import br.com.pokeidle.shared.domain.NotFoundException;
import br.com.pokeidle.treinadores.domain.TreinadorNpcPokemonRepository;
import br.com.pokeidle.treinadores.domain.TreinadorNpcRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ObterTreinadoresDoNoHandler {

    private final NoJornadaRepository noJornadaRepository;
    private final TreinadorNpcRepository treinadorNpcRepository;
    private final TreinadorNpcPokemonRepository treinadorNpcPokemonRepository;

    public ObterTreinadoresDoNoHandler(NoJornadaRepository noJornadaRepository,
                                       TreinadorNpcRepository treinadorNpcRepository,
                                       TreinadorNpcPokemonRepository treinadorNpcPokemonRepository) {
        this.noJornadaRepository = noJornadaRepository;
        this.treinadorNpcRepository = treinadorNpcRepository;
        this.treinadorNpcPokemonRepository = treinadorNpcPokemonRepository;
    }

    public java.util.List<TreinadorDoNoDto> handle(ObterTreinadoresDoNoQuery query) {
        noJornadaRepository.findById(query.noId())
                .orElseThrow(() -> new NotFoundException("No nao encontrado."));

        return treinadorNpcRepository.findByNoJornadaIdOrderByOrdemDesafioAsc(query.noId()).stream()
                .map(treinador -> new TreinadorDoNoDto(
                        treinador.getId(),
                        treinador.getNome(),
                        treinador.getNoJornadaId(),
                        treinador.getGinasioId(),
                        treinador.isLider(),
                        treinador.getOrdemDesafio(),
                        treinador.getRecompensaMoedas(),
                        treinador.getExperienciaRecompensa(),
                        treinadorNpcPokemonRepository.findByTreinadorNpcIdOrderByOrdemEquipeAsc(treinador.getId()).stream()
                                .map(pokemon -> new TreinadorDoNoDto.PokemonTreinadorDto(
                                        pokemon.getEspecieId(),
                                        pokemon.getNome(),
                                        pokemon.getNivel(),
                                        pokemon.getOrdemEquipe()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
