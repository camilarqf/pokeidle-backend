package br.com.pokeidle.mundo.domain;

import java.util.List;

public interface ObjetivoMissaoNoRepository {

    List<ObjetivoMissaoNo> findByMissaoNoIdOrderByOrdemAsc(Long missaoNoId);
}
