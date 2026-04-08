package br.com.pokeidle.treinadores.infrastructure;

import br.com.pokeidle.treinadores.domain.Ginasio;
import br.com.pokeidle.treinadores.domain.GinasioRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaGinasioRepository extends JpaRepository<Ginasio, Long>, GinasioRepository {

    @Override
    Optional<Ginasio> findByNoJornadaId(Long noJornadaId);
}
