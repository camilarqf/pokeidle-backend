package br.com.pokeidle.inventario.infrastructure;

import br.com.pokeidle.inventario.domain.CidadeItemLoja;
import br.com.pokeidle.inventario.domain.CidadeItemLojaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaCidadeItemLojaRepository extends JpaRepository<CidadeItemLoja, Long>, CidadeItemLojaRepository {

    @Override
    List<CidadeItemLoja> findByCidadeId(Long cidadeId);

    @Override
    Optional<CidadeItemLoja> findByCidadeIdAndItemId(Long cidadeId, Long itemId);
}
