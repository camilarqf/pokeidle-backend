package br.com.pokeidle.jogador.infrastructure;

import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaJogadorRepository extends JpaRepository<Jogador, String>, JogadorRepository {

    @Override
    boolean existsByNomePerfilIgnoreCase(String nomePerfil);
}
