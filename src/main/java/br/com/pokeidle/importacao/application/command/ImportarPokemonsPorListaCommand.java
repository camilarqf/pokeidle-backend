package br.com.pokeidle.importacao.application.command;

import java.util.List;

public record ImportarPokemonsPorListaCommand(List<String> nomesOuIds) {
}
