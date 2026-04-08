package br.com.pokeidle.treinadores.domain;

import java.util.List;
import java.util.Optional;

public interface JogadorTreinadorDerrotadoRepository {

    JogadorTreinadorDerrotado save(JogadorTreinadorDerrotado registro);

    Optional<JogadorTreinadorDerrotado> findByJogadorIdAndTreinadorNpcId(String jogadorId, Long treinadorNpcId);

    List<JogadorTreinadorDerrotado> findByJogadorId(String jogadorId);
}
