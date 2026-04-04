package br.com.pokeidle.inventario.domain;

import java.util.List;
import java.util.Optional;

public interface InventarioJogadorRepository {

    InventarioJogador save(InventarioJogador inventarioJogador);

    Optional<InventarioJogador> findByJogadorIdAndItemId(String jogadorId, Long itemId);

    List<InventarioJogador> findByJogadorId(String jogadorId);
}
