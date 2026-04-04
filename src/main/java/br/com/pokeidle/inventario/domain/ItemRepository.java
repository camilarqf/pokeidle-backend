package br.com.pokeidle.inventario.domain;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> findById(Long id);

    Optional<Item> findByCodigo(CodigoItem codigo);

    List<Item> findAllByIdIn(List<Long> ids);
}
