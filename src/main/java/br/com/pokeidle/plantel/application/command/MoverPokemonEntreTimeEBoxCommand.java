package br.com.pokeidle.plantel.application.command;

public record MoverPokemonEntreTimeEBoxCommand(String jogadorId,
                                               String pokemonId,
                                               boolean paraBox,
                                               Integer slotDestino) {
}
