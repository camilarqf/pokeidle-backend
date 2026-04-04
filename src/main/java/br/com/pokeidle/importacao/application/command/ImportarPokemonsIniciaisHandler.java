package br.com.pokeidle.importacao.application.command;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportarPokemonsIniciaisHandler {

    private static final List<String> POKEMONS_INICIAIS = List.of("bulbasaur", "charmander", "squirtle", "pidgey", "rattata");

    private final ImportarPokemonPorIdOuNomeHandler importarPokemonPorIdOuNomeHandler;

    public ImportarPokemonsIniciaisHandler(ImportarPokemonPorIdOuNomeHandler importarPokemonPorIdOuNomeHandler) {
        this.importarPokemonPorIdOuNomeHandler = importarPokemonPorIdOuNomeHandler;
    }

    public List<PokemonImportadoDto> handle(ImportarPokemonsIniciaisCommand command) {
        return POKEMONS_INICIAIS.stream()
                .map(nome -> importarPokemonPorIdOuNomeHandler.handle(new ImportarPokemonPorIdOuNomeCommand(nome)))
                .toList();
    }
}
