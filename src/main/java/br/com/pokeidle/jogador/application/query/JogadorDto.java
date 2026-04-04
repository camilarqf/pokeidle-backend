package br.com.pokeidle.jogador.application.query;

public record JogadorDto(String id,
                         String nomePerfil,
                         int saldoMoedas,
                         Long noAtualId,
                         boolean pokemonInicialEscolhido) {
}
