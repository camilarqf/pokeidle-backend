package br.com.pokeidle.jogador.domain;

import java.util.Optional;

public interface JogadorRepository {

    Jogador save(Jogador jogador);

    Optional<Jogador> findById(String id);

    boolean existsByNomePerfilIgnoreCase(String nomePerfil);
}
