package br.com.pokeidle.plantel.infrastructure;

import br.com.pokeidle.plantel.domain.TimeAtivoSlot;
import br.com.pokeidle.plantel.domain.TimeAtivoSlotRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaTimeAtivoSlotRepository extends JpaRepository<TimeAtivoSlot, Long>, TimeAtivoSlotRepository {

    @Override
    List<TimeAtivoSlot> findByJogadorIdOrderBySlotNumeroAsc(String jogadorId);

    @Override
    Optional<TimeAtivoSlot> findByJogadorIdAndSlotNumero(String jogadorId, int slotNumero);

    @Override
    Optional<TimeAtivoSlot> findByPokemonCapturadoId(String pokemonCapturadoId);
}
