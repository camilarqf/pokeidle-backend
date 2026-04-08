package br.com.pokeidle.batalha.domain;

import java.util.List;
import java.util.Optional;

public interface BatalhaOponentePokemonRepository {

    BatalhaOponentePokemon save(BatalhaOponentePokemon oponente);

    List<BatalhaOponentePokemon> findByBatalhaIdOrderByOrdemEquipeAsc(String batalhaId);

    Optional<BatalhaOponentePokemon> findByBatalhaIdAndOrdemEquipe(String batalhaId, int ordemEquipe);
}
