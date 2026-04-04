package br.com.pokeidle.mundo.infrastructure;

import br.com.pokeidle.mundo.domain.NoJornada;
import br.com.pokeidle.mundo.domain.NoJornadaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaNoJornadaRepository extends JpaRepository<NoJornada, Long>, NoJornadaRepository {

    @Override
    List<NoJornada> findAllByOrderByOrdemMapaAsc();
}
