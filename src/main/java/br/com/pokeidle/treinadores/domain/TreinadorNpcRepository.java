package br.com.pokeidle.treinadores.domain;

import java.util.List;
import java.util.Optional;

public interface TreinadorNpcRepository {

    Optional<TreinadorNpc> findById(Long id);

    List<TreinadorNpc> findByNoJornadaIdOrderByOrdemDesafioAsc(Long noJornadaId);

    List<TreinadorNpc> findByGinasioIdOrderByOrdemDesafioAsc(Long ginasioId);
}
