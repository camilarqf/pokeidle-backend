package br.com.pokeidle.inventario.infrastructure;

import br.com.pokeidle.inventario.domain.InventarioJogador;
import br.com.pokeidle.inventario.domain.InventarioJogadorRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaInventarioJogadorRepository extends JpaRepository<InventarioJogador, String>, InventarioJogadorRepository {

    @Override
    Optional<InventarioJogador> findByJogadorIdAndItemId(String jogadorId, Long itemId);

    @Override
    List<InventarioJogador> findByJogadorId(String jogadorId);
}
