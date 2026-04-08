package br.com.pokeidle.plantel.domain;

import java.util.List;
import java.util.Optional;

public interface TimeAtivoSlotRepository {

    TimeAtivoSlot save(TimeAtivoSlot slot);

    void delete(TimeAtivoSlot slot);

    void flush();

    List<TimeAtivoSlot> findByJogadorIdOrderBySlotNumeroAsc(String jogadorId);

    Optional<TimeAtivoSlot> findByJogadorIdAndSlotNumero(String jogadorId, int slotNumero);

    Optional<TimeAtivoSlot> findByPokemonCapturadoId(String pokemonCapturadoId);
}
