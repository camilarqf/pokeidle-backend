package br.com.pokeidle.treinadores.domain;

import java.util.List;

public interface TreinadorNpcPokemonRepository {

    List<TreinadorNpcPokemon> findByTreinadorNpcIdOrderByOrdemEquipeAsc(Long treinadorNpcId);
}
