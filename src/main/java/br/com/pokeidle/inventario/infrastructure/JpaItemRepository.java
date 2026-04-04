package br.com.pokeidle.inventario.infrastructure;

import br.com.pokeidle.inventario.domain.CodigoItem;
import br.com.pokeidle.inventario.domain.Item;
import br.com.pokeidle.inventario.domain.ItemRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaItemRepository extends JpaRepository<Item, Long>, ItemRepository {

    @Override
    Optional<Item> findByCodigo(CodigoItem codigo);

    @Override
    List<Item> findAllByIdIn(List<Long> ids);
}
