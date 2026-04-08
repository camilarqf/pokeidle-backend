package br.com.pokeidle.batalha.api;

import br.com.pokeidle.batalha.application.command.IniciarBatalhaSelvagemCommand;
import br.com.pokeidle.batalha.application.command.IniciarBatalhaLiderCommand;
import br.com.pokeidle.batalha.application.command.IniciarBatalhaTreinadorCommand;
import br.com.pokeidle.batalha.application.command.IniciarBatalhaTreinadorHandler;
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
    private final IniciarBatalhaTreinadorHandler iniciarBatalhaTreinadorHandler;
    private final ResolverTurnoBatalhaHandler resolverTurnoBatalhaHandler;
    private final ObterBatalhaAtualHandler obterBatalhaAtualHandler;

    public BatalhaController(IniciarBatalhaSelvagemHandler iniciarBatalhaSelvagemHandler,
                             IniciarBatalhaTreinadorHandler iniciarBatalhaTreinadorHandler,
                             ResolverTurnoBatalhaHandler resolverTurnoBatalhaHandler,
                             ObterBatalhaAtualHandler obterBatalhaAtualHandler) {
        this.iniciarBatalhaSelvagemHandler = iniciarBatalhaSelvagemHandler;
        this.iniciarBatalhaTreinadorHandler = iniciarBatalhaTreinadorHandler;
        this.resolverTurnoBatalhaHandler = resolverTurnoBatalhaHandler;
        this.obterBatalhaAtualHandler = obterBatalhaAtualHandler;
    }

    @PostMapping("/jogadores/{id}/batalhas/selvagem/iniciar")
    public BatalhaDto iniciar(@PathVariable String id) {
        return iniciarBatalhaSelvagemHandler.handle(new IniciarBatalhaSelvagemCommand(id));
    }

    @PostMapping("/jogadores/{id}/batalhas/treinador/{treinadorId}/iniciar")
    public BatalhaDto iniciarTreinador(@PathVariable String id, @PathVariable Long treinadorId) {
        return iniciarBatalhaTreinadorHandler.handle(new IniciarBatalhaTreinadorCommand(id, treinadorId));
    }

    @PostMapping("/jogadores/{id}/batalhas/lider/{liderId}/iniciar")
    public BatalhaDto iniciarLider(@PathVariable String id, @PathVariable Long liderId) {
        return iniciarBatalhaTreinadorHandler.handle(new IniciarBatalhaLiderCommand(id, liderId));
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
