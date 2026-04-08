package br.com.pokeidle.jogador.infrastructure;

import br.com.pokeidle.jogador.domain.BadgeJogador;
import br.com.pokeidle.jogador.domain.BadgeJogadorRepository;
import br.com.pokeidle.jogador.domain.CodigoBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaBadgeJogadorRepository extends JpaRepository<BadgeJogador, Long>, BadgeJogadorRepository {

    @Override
    Optional<BadgeJogador> findByJogadorIdAndCodigo(String jogadorId, CodigoBadge codigo);

    @Override
    List<BadgeJogador> findByJogadorIdOrderByObtidaEmAsc(String jogadorId);
}
