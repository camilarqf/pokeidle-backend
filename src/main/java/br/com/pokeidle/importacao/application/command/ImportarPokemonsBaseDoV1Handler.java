package br.com.pokeidle.importacao.application.command;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportarPokemonsBaseDoV1Handler {

    private static final List<String> BASE_V1 = List.of(
            "bulbasaur",
            "charmander",
            "squirtle",
            "pidgey",
            "rattata",
            "caterpie",
            "weedle",
            "pikachu",
            "geodude",
            "sandshrew",
            "onix"
    );

    private final ImportarPokemonsPorListaHandler importarPokemonsPorListaHandler;

    public ImportarPokemonsBaseDoV1Handler(ImportarPokemonsPorListaHandler importarPokemonsPorListaHandler) {
        this.importarPokemonsPorListaHandler = importarPokemonsPorListaHandler;
    }

    public List<PokemonImportadoDto> handle(ImportarPokemonsBaseDoV1Command command) {
        return importarPokemonsPorListaHandler.handle(new ImportarPokemonsPorListaCommand(BASE_V1));
    }
}
