package br.com.pokeidle.progresso.domain;

import java.util.List;
import java.util.Optional;

public interface ProgressoNoJogadorRepository {

    ProgressoNoJogador save(ProgressoNoJogador progressoNoJogador);

    List<ProgressoNoJogador> findByJogadorIdOrderByNoJornadaIdAsc(String jogadorId);

    Optional<ProgressoNoJogador> findByJogadorIdAndNoJornadaId(String jogadorId, Long noJornadaId);
}
