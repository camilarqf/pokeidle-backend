package br.com.pokeidle.treinadores.infrastructure;

import br.com.pokeidle.treinadores.domain.JogadorTreinadorDerrotado;
import br.com.pokeidle.treinadores.domain.JogadorTreinadorDerrotadoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaJogadorTreinadorDerrotadoRepository extends JpaRepository<JogadorTreinadorDerrotado, Long>, JogadorTreinadorDerrotadoRepository {

    @Override
    Optional<JogadorTreinadorDerrotado> findByJogadorIdAndTreinadorNpcId(String jogadorId, Long treinadorNpcId);

    @Override
    List<JogadorTreinadorDerrotado> findByJogadorId(String jogadorId);
}
