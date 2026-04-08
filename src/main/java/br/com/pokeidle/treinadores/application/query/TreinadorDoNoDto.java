package br.com.pokeidle.treinadores.application.query;

import java.util.List;

public record TreinadorDoNoDto(Long id,
                               String nome,
                               Long noJornadaId,
                               Long ginasioId,
                               boolean lider,
                               int ordemDesafio,
                               int recompensaMoedas,
                               int experienciaRecompensa,
                               List<PokemonTreinadorDto> equipe) {

    public record PokemonTreinadorDto(Long especieId,
                                      String nome,
                                      int nivel,
                                      int ordemEquipe) {
    }
}
