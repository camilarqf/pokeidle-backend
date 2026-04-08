package br.com.pokeidle.treinadores.infrastructure;

import br.com.pokeidle.treinadores.domain.TreinadorNpcPokemon;
import br.com.pokeidle.treinadores.domain.TreinadorNpcPokemonRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTreinadorNpcPokemonRepository extends JpaRepository<TreinadorNpcPokemon, Long>, TreinadorNpcPokemonRepository {

    @Override
    List<TreinadorNpcPokemon> findByTreinadorNpcIdOrderByOrdemEquipeAsc(Long treinadorNpcId);
}
