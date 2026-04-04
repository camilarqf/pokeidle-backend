package br.com.pokeidle.inventario.domain;

import java.util.List;
import java.util.Optional;

public interface CidadeItemLojaRepository {

    List<CidadeItemLoja> findByCidadeId(Long cidadeId);

    Optional<CidadeItemLoja> findByCidadeIdAndItemId(Long cidadeId, Long itemId);
}
