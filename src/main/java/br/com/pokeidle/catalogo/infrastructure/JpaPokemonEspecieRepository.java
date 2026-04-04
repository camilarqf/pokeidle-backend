package br.com.pokeidle.catalogo.infrastructure;

import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.catalogo.domain.PokemonEspecieRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaPokemonEspecieRepository extends JpaRepository<PokemonEspecie, Long>, PokemonEspecieRepository {

    @Override
    Optional<PokemonEspecie> findByNomeIgnoreCase(String nome);

    @Override
    List<PokemonEspecie> findAllByIdIn(List<Long> ids);
}
