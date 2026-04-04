package br.com.pokeidle.batalha.domain;

import java.util.Optional;

public interface BatalhaRepository {

    Batalha save(Batalha batalha);

    Optional<Batalha> findById(String id);

    Optional<Batalha> findFirstByJogadorIdAndStatus(String jogadorId, StatusBatalha status);
}
