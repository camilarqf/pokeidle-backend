package br.com.pokeidle.plantel.domain;

import java.util.List;
import java.util.Optional;

public interface BoxPokemonRepository {

    BoxPokemon save(BoxPokemon boxPokemon);

    void delete(BoxPokemon boxPokemon);

    List<BoxPokemon> findByJogadorIdOrderByArmazenadoEmAsc(String jogadorId);

    Optional<BoxPokemon> findByPokemonCapturadoId(String pokemonCapturadoId);
}
