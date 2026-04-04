package br.com.pokeidle.batalha.api;

import br.com.pokeidle.batalha.application.command.IniciarBatalhaSelvagemCommand;
import br.com.pokeidle.batalha.application.command.IniciarBatalhaSelvagemHandler;
import br.com.pokeidle.batalha.application.command.ResolverTurnoBatalhaCommand;
import br.com.pokeidle.batalha.application.command.ResolverTurnoBatalhaHandler;
import br.com.pokeidle.batalha.application.query.BatalhaDto;
import br.com.pokeidle.batalha.application.query.ObterBatalhaAtualHandler;
import br.com.pokeidle.batalha.application.query.ObterBatalhaAtualQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatalhaController {

    private final IniciarBatalhaSelvagemHandler iniciarBatalhaSelvagemHandler;
    private final ResolverTurnoBatalhaHandler resolverTurnoBatalhaHandler;
    private final ObterBatalhaAtualHandler obterBatalhaAtualHandler;

    public BatalhaController(IniciarBatalhaSelvagemHandler iniciarBatalhaSelvagemHandler,
                             ResolverTurnoBatalhaHandler resolverTurnoBatalhaHandler,
                             ObterBatalhaAtualHandler obterBatalhaAtualHandler) {
        this.iniciarBatalhaSelvagemHandler = iniciarBatalhaSelvagemHandler;
        this.resolverTurnoBatalhaHandler = resolverTurnoBatalhaHandler;
        this.obterBatalhaAtualHandler = obterBatalhaAtualHandler;
    }

    @PostMapping("/jogadores/{id}/batalhas/selvagem/iniciar")
    public BatalhaDto iniciar(@PathVariable String id) {
        return iniciarBatalhaSelvagemHandler.handle(new IniciarBatalhaSelvagemCommand(id));
    }

    @PostMapping("/batalhas/{id}/turno")
    public BatalhaDto resolverTurno(@PathVariable String id) {
        return resolverTurnoBatalhaHandler.handle(new ResolverTurnoBatalhaCommand(id));
    }

    @GetMapping("/batalhas/{id}")
    public BatalhaDto obter(@PathVariable String id) {
        return obterBatalhaAtualHandler.handle(new ObterBatalhaAtualQuery(id));
    }
}
