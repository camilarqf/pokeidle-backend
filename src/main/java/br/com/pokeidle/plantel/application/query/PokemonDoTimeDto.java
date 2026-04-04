package br.com.pokeidle.plantel.application.query;

public record PokemonDoTimeDto(String id,
                               Long especieId,
                               String nome,
                               int nivel,
                               int experiencia,
                               int hpAtual,
                               int hpMaximo,
                               int ataque,
                               int defesa,
                               int velocidade,
                               boolean inicial,
                               boolean ativo) {
}
