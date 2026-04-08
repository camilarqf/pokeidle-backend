package br.com.pokeidle.treinadores.api;

import br.com.pokeidle.treinadores.application.query.GinasioDto;
import br.com.pokeidle.treinadores.application.query.ObterGinasioHandler;
import br.com.pokeidle.treinadores.application.query.ObterStatusDoGinasioHandler;
import br.com.pokeidle.treinadores.application.query.ObterStatusDoGinasioQuery;
import br.com.pokeidle.treinadores.application.query.StatusDoGinasioDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GinasioController {

    private final ObterGinasioHandler obterGinasioHandler;
    private final ObterStatusDoGinasioHandler obterStatusDoGinasioHandler;

    public GinasioController(ObterGinasioHandler obterGinasioHandler,
                             ObterStatusDoGinasioHandler obterStatusDoGinasioHandler) {
        this.obterGinasioHandler = obterGinasioHandler;
        this.obterStatusDoGinasioHandler = obterStatusDoGinasioHandler;
    }

    @GetMapping("/ginasios/{ginasioId}")
    public GinasioDto obter(@PathVariable Long ginasioId) {
        return obterGinasioHandler.handle(ginasioId);
    }

    @GetMapping("/jogadores/{id}/ginasios/{ginasioId}/status")
    public StatusDoGinasioDto status(@PathVariable String id, @PathVariable Long ginasioId) {
        return obterStatusDoGinasioHandler.handle(new ObterStatusDoGinasioQuery(id, ginasioId));
    }
}
