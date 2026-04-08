package br.com.pokeidle.plantel.domain;

import java.util.List;
import java.util.Optional;

public interface PokemonCapturadoRepository {

    PokemonCapturado save(PokemonCapturado pokemonCapturado);

    List<PokemonCapturado> findByJogadorIdOrderByCapturadoEmAsc(String jogadorId);

    Optional<PokemonCapturado> findById(String id);

    Optional<PokemonCapturado> findFirstByJogadorIdAndAtivoTrue(String jogadorId);

    List<PokemonCapturado> findByIdIn(List<String> ids);
}
