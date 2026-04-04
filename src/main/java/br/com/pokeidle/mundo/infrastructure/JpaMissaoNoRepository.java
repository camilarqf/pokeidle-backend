package br.com.pokeidle.mundo.infrastructure;

import br.com.pokeidle.mundo.domain.MissaoNo;
import br.com.pokeidle.mundo.domain.MissaoNoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaMissaoNoRepository extends JpaRepository<MissaoNo, Long>, MissaoNoRepository {

    @Override
    Optional<MissaoNo> findByNoJornadaId(Long noJornadaId);
}
