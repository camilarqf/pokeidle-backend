package br.com.pokeidle.mundo.infrastructure;

import br.com.pokeidle.mundo.domain.NoConexao;
import br.com.pokeidle.mundo.domain.NoConexaoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaNoConexaoRepository extends JpaRepository<NoConexao, Long>, NoConexaoRepository {

    @Override
    List<NoConexao> findByOrigemNoId(Long origemNoId);
}
