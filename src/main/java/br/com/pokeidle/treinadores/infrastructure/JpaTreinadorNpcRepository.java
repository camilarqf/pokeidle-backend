package br.com.pokeidle.treinadores.infrastructure;

import br.com.pokeidle.treinadores.domain.TreinadorNpc;
import br.com.pokeidle.treinadores.domain.TreinadorNpcRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTreinadorNpcRepository extends JpaRepository<TreinadorNpc, Long>, TreinadorNpcRepository {

    @Override
    List<TreinadorNpc> findByNoJornadaIdOrderByOrdemDesafioAsc(Long noJornadaId);

    @Override
    List<TreinadorNpc> findByGinasioIdOrderByOrdemDesafioAsc(Long ginasioId);
}
