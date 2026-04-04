package br.com.pokeidle.mundo.domain;

import java.util.List;

public interface NoConexaoRepository {

    List<NoConexao> findByOrigemNoId(Long origemNoId);
}
