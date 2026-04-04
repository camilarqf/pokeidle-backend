package br.com.pokeidle.batalha.infrastructure;

import br.com.pokeidle.batalha.domain.Batalha;
import br.com.pokeidle.batalha.domain.BatalhaRepository;
import br.com.pokeidle.batalha.domain.StatusBatalha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaBatalhaRepository extends JpaRepository<Batalha, String>, BatalhaRepository {

    @Override
    Optional<Batalha> findFirstByJogadorIdAndStatus(String jogadorId, StatusBatalha status);
}
