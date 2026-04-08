package br.com.pokeidle.jogador.domain;

import java.util.List;
import java.util.Optional;

public interface BadgeJogadorRepository {

    BadgeJogador save(BadgeJogador badgeJogador);

    Optional<BadgeJogador> findByJogadorIdAndCodigo(String jogadorId, CodigoBadge codigo);

    List<BadgeJogador> findByJogadorIdOrderByObtidaEmAsc(String jogadorId);
}
