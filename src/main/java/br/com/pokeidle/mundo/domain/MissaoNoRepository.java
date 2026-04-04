package br.com.pokeidle.mundo.domain;

import java.util.Optional;

public interface MissaoNoRepository {

    Optional<MissaoNo> findByNoJornadaId(Long noJornadaId);
}
