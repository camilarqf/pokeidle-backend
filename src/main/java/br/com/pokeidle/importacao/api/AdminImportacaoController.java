package br.com.pokeidle.importacao.api;

import br.com.pokeidle.importacao.application.command.ImportarPokemonPorIdOuNomeCommand;
import br.com.pokeidle.importacao.application.command.ImportarPokemonPorIdOuNomeHandler;
import br.com.pokeidle.importacao.application.command.ImportarPokemonsIniciaisCommand;
import br.com.pokeidle.importacao.application.command.ImportarPokemonsIniciaisHandler;
import br.com.pokeidle.importacao.application.command.PokemonImportadoDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/importacoes")
public class AdminImportacaoController {

    private final ImportarPokemonPorIdOuNomeHandler importarPokemonPorIdOuNomeHandler;
    private final ImportarPokemonsIniciaisHandler importarPokemonsIniciaisHandler;

    public AdminImportacaoController(ImportarPokemonPorIdOuNomeHandler importarPokemonPorIdOuNomeHandler,
                                     ImportarPokemonsIniciaisHandler importarPokemonsIniciaisHandler) {
        this.importarPokemonPorIdOuNomeHandler = importarPokemonPorIdOuNomeHandler;
        this.importarPokemonsIniciaisHandler = importarPokemonsIniciaisHandler;
    }

    @PostMapping("/pokemon/{nomeOuId}")
    public PokemonImportadoDto importarPokemon(@PathVariable String nomeOuId) {
        return importarPokemonPorIdOuNomeHandler.handle(new ImportarPokemonPorIdOuNomeCommand(nomeOuId));
    }

    @PostMapping("/pokemon/iniciais")
    public List<PokemonImportadoDto> importarPokemonsIniciais() {
        return importarPokemonsIniciaisHandler.handle(new ImportarPokemonsIniciaisCommand());
    }
}
