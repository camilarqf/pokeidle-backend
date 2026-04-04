package br.com.pokeidle.catalogo.infrastructure;

import br.com.pokeidle.catalogo.domain.PokemonPerfilDesign;
import br.com.pokeidle.catalogo.domain.PokemonPerfilDesignRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaPokemonPerfilDesignRepository extends JpaRepository<PokemonPerfilDesign, Long>, PokemonPerfilDesignRepository {

    @Override
    Optional<PokemonPerfilDesign> findByPokemonEspecieId(Long pokemonEspecieId);
}
