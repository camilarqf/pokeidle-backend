package br.com.pokeidle.catalogo.domain;

import java.util.Optional;

public interface PokemonPerfilDesignRepository {

    PokemonPerfilDesign save(PokemonPerfilDesign pokemonPerfilDesign);

    Optional<PokemonPerfilDesign> findByPokemonEspecieId(Long pokemonEspecieId);
}
