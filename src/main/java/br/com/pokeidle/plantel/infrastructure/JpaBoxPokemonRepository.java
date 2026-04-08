package br.com.pokeidle.plantel.infrastructure;

import br.com.pokeidle.plantel.domain.BoxPokemon;
import br.com.pokeidle.plantel.domain.BoxPokemonRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaBoxPokemonRepository extends JpaRepository<BoxPokemon, Long>, BoxPokemonRepository {

    @Override
    List<BoxPokemon> findByJogadorIdOrderByArmazenadoEmAsc(String jogadorId);

    @Override
    Optional<BoxPokemon> findByPokemonCapturadoId(String pokemonCapturadoId);
}
