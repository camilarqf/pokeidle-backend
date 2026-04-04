package br.com.pokeidle.mundo.domain;

import java.util.List;
import java.util.Optional;

public interface NoJornadaRepository {

    Optional<NoJornada> findById(Long id);

    List<NoJornada> findAllByOrderByOrdemMapaAsc();
}
