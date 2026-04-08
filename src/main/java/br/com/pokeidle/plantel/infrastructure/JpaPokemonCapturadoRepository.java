package br.com.pokeidle.plantel.infrastructure;

import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaPokemonCapturadoRepository extends JpaRepository<PokemonCapturado, String>, PokemonCapturadoRepository {

    @Override
    List<PokemonCapturado> findByJogadorIdOrderByCapturadoEmAsc(String jogadorId);

    @Override
    Optional<PokemonCapturado> findFirstByJogadorIdAndAtivoTrue(String jogadorId);

    @Override
    List<PokemonCapturado> findByIdIn(List<String> ids);
}
