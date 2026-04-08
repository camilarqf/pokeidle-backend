package br.com.pokeidle.catalogo.domain;

import java.util.List;
import java.util.Optional;

public interface PokemonEspecieRepository {

    PokemonEspecie save(PokemonEspecie pokemonEspecie);

    Optional<PokemonEspecie> findById(Long id);

    Optional<PokemonEspecie> findByNomeIgnoreCase(String nome);

    List<PokemonEspecie> findAllByIdIn(List<Long> ids);

    List<PokemonEspecie> findAll();
}
