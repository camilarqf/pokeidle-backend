package br.com.pokeidle.progresso.infrastructure;

import br.com.pokeidle.progresso.domain.ProgressoNoJogador;
import br.com.pokeidle.progresso.domain.ProgressoNoJogadorRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaProgressoNoJogadorRepository extends JpaRepository<ProgressoNoJogador, String>, ProgressoNoJogadorRepository {

    @Override
    List<ProgressoNoJogador> findByJogadorIdOrderByNoJornadaIdAsc(String jogadorId);

    @Override
    Optional<ProgressoNoJogador> findByJogadorIdAndNoJornadaId(String jogadorId, Long noJornadaId);
}
