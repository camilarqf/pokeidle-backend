package br.com.pokeidle.batalha.application.query;

public record BatalhaDto(String id,
                         String jogadorId,
                         Long noJornadaId,
                         String pokemonJogadorId,
                         Long especieSelvagemId,
                         String nomeSelvagem,
                         int nivelSelvagem,
                         int hpAtualSelvagem,
                         int hpMaximoSelvagem,
                         String status,
                         int turnos,
                         int experienciaConcedida) {
}
