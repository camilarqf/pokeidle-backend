package br.com.pokeidle.plantel.application.command;

import java.util.List;

public record DefinirTimeAtivoCommand(String jogadorId, List<String> pokemonIdsOrdenados) {
}
