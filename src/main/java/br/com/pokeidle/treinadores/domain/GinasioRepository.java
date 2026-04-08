package br.com.pokeidle.treinadores.domain;

import java.util.Optional;

public interface GinasioRepository {

    Optional<Ginasio> findById(Long id);

    Optional<Ginasio> findByNoJornadaId(Long noJornadaId);
}
