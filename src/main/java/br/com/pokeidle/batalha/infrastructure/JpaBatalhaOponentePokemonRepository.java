package br.com.pokeidle.batalha.infrastructure;

import br.com.pokeidle.batalha.domain.BatalhaOponentePokemon;
import br.com.pokeidle.batalha.domain.BatalhaOponentePokemonRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaBatalhaOponentePokemonRepository extends JpaRepository<BatalhaOponentePokemon, Long>, BatalhaOponentePokemonRepository {

    @Override
    List<BatalhaOponentePokemon> findByBatalhaIdOrderByOrdemEquipeAsc(String batalhaId);

    @Override
    Optional<BatalhaOponentePokemon> findByBatalhaIdAndOrdemEquipe(String batalhaId, int ordemEquipe);
}
