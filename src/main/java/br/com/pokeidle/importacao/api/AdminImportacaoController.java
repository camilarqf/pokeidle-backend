package br.com.pokeidle.importacao.api;

import br.com.pokeidle.importacao.application.command.ImportarPokemonPorIdOuNomeCommand;
import br.com.pokeidle.importacao.application.command.ImportarPokemonPorIdOuNomeHandler;
import br.com.pokeidle.importacao.application.command.ImportarPokemonsBaseDoV1Command;
import br.com.pokeidle.importacao.application.command.ImportarPokemonsBaseDoV1Handler;
import br.com.pokeidle.importacao.application.command.ImportarPokemonsPorListaCommand;
import br.com.pokeidle.importacao.application.command.ImportarPokemonsPorListaHandler;
import br.com.pokeidle.importacao.application.command.PokemonImportadoDto;
import br.com.pokeidle.importacao.application.command.ReprocessarPerfisPokemonCommand;
import br.com.pokeidle.importacao.application.command.ReprocessarPerfisPokemonHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/importacoes")
public class AdminImportacaoController {

    private final ImportarPokemonPorIdOuNomeHandler importarPokemonPorIdOuNomeHandler;
    private final ImportarPokemonsPorListaHandler importarPokemonsPorListaHandler;
    private final ImportarPokemonsBaseDoV1Handler importarPokemonsBaseDoV1Handler;
    private final ReprocessarPerfisPokemonHandler reprocessarPerfisPokemonHandler;

    public AdminImportacaoController(ImportarPokemonPorIdOuNomeHandler importarPokemonPorIdOuNomeHandler,
                                     ImportarPokemonsPorListaHandler importarPokemonsPorListaHandler,
                                     ImportarPokemonsBaseDoV1Handler importarPokemonsBaseDoV1Handler,
                                     ReprocessarPerfisPokemonHandler reprocessarPerfisPokemonHandler) {
        this.importarPokemonPorIdOuNomeHandler = importarPokemonPorIdOuNomeHandler;
        this.importarPokemonsPorListaHandler = importarPokemonsPorListaHandler;
        this.importarPokemonsBaseDoV1Handler = importarPokemonsBaseDoV1Handler;
        this.reprocessarPerfisPokemonHandler = reprocessarPerfisPokemonHandler;
    }

    @PostMapping("/pokemon/{nomeOuId}")
    public PokemonImportadoDto importarPokemon(@PathVariable String nomeOuId) {
        return importarPokemonPorIdOuNomeHandler.handle(new ImportarPokemonPorIdOuNomeCommand(nomeOuId));
    }

    @PostMapping("/pokemon/lista")
    public List<PokemonImportadoDto> importarLista(@RequestBody ImportarListaRequest request) {
        return importarPokemonsPorListaHandler.handle(new ImportarPokemonsPorListaCommand(request.nomesOuIds()));
    }

    @PostMapping("/pokemon/base-v1")
    public List<PokemonImportadoDto> importarBaseV1() {
        return importarPokemonsBaseDoV1Handler.handle(new ImportarPokemonsBaseDoV1Command());
    }

    @PostMapping("/pokemon/reprocessar-perfis")
    public ResponseEntity<List<Long>> reprocessarPerfis() {
        return ResponseEntity.ok(reprocessarPerfisPokemonHandler.handle(new ReprocessarPerfisPokemonCommand()));
    }

    public record ImportarListaRequest(List<String> nomesOuIds) {
    }
}
