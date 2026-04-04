package br.com.pokeidle.plantel.api;

import br.com.pokeidle.plantel.application.query.ObterTimeDoJogadorHandler;
import br.com.pokeidle.plantel.application.query.PokemonDoTimeDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TimeController {

    private final ObterTimeDoJogadorHandler obterTimeDoJogadorHandler;

    public TimeController(ObterTimeDoJogadorHandler obterTimeDoJogadorHandler) {
        this.obterTimeDoJogadorHandler = obterTimeDoJogadorHandler;
    }

    @GetMapping("/jogadores/{id}/time")
    public List<PokemonDoTimeDto> obterTime(@PathVariable String id) {
        return obterTimeDoJogadorHandler.handle(id);
    }
}
