package br.com.pokeidle.mundo.infrastructure;

import br.com.pokeidle.mundo.domain.Regiao;
import br.com.pokeidle.mundo.domain.RegiaoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRegiaoRepository extends JpaRepository<Regiao, Long>, RegiaoRepository {
}
