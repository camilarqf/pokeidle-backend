package br.com.pokeidle.importacao.application.command;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportarPokemonsPorListaHandler {

    private final ImportarPokemonPorIdOuNomeHandler importarPokemonPorIdOuNomeHandler;

    public ImportarPokemonsPorListaHandler(ImportarPokemonPorIdOuNomeHandler importarPokemonPorIdOuNomeHandler) {
        this.importarPokemonPorIdOuNomeHandler = importarPokemonPorIdOuNomeHandler;
    }

    public List<PokemonImportadoDto> handle(ImportarPokemonsPorListaCommand command) {
        return command.nomesOuIds().stream()
                .map(nomeOuId -> importarPokemonPorIdOuNomeHandler.handle(new ImportarPokemonPorIdOuNomeCommand(nomeOuId)))
                .toList();
    }
}
