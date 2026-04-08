package br.com.pokeidle.mundo.infrastructure;

import br.com.pokeidle.mundo.domain.ObjetivoMissaoNo;
import br.com.pokeidle.mundo.domain.ObjetivoMissaoNoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaObjetivoMissaoNoRepository extends JpaRepository<ObjetivoMissaoNo, Long>, ObjetivoMissaoNoRepository {

    @Override
    List<ObjetivoMissaoNo> findByMissaoNoIdOrderByOrdemAsc(Long missaoNoId);
}
