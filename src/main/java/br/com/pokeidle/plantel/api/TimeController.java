package br.com.pokeidle.plantel.api;

import br.com.pokeidle.plantel.application.command.DefinirTimeAtivoCommand;
import br.com.pokeidle.plantel.application.command.DefinirTimeAtivoHandler;
import br.com.pokeidle.plantel.application.command.MoverPokemonEntreTimeEBoxCommand;
import br.com.pokeidle.plantel.application.command.MoverPokemonEntreTimeEBoxHandler;
import br.com.pokeidle.plantel.application.query.ObterBoxPokemonHandler;
import br.com.pokeidle.plantel.application.query.ObterBoxPokemonQuery;
import br.com.pokeidle.plantel.application.query.ObterTimeAtivoHandler;
import br.com.pokeidle.plantel.application.query.ObterTimeAtivoQuery;
import br.com.pokeidle.plantel.application.query.PokemonDoTimeDto;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TimeController {

    private final ObterTimeAtivoHandler obterTimeAtivoHandler;
    private final ObterBoxPokemonHandler obterBoxPokemonHandler;
    private final DefinirTimeAtivoHandler definirTimeAtivoHandler;
    private final MoverPokemonEntreTimeEBoxHandler moverPokemonEntreTimeEBoxHandler;

    public TimeController(ObterTimeAtivoHandler obterTimeAtivoHandler,
                          ObterBoxPokemonHandler obterBoxPokemonHandler,
                          DefinirTimeAtivoHandler definirTimeAtivoHandler,
                          MoverPokemonEntreTimeEBoxHandler moverPokemonEntreTimeEBoxHandler) {
        this.obterTimeAtivoHandler = obterTimeAtivoHandler;
        this.obterBoxPokemonHandler = obterBoxPokemonHandler;
        this.definirTimeAtivoHandler = definirTimeAtivoHandler;
        this.moverPokemonEntreTimeEBoxHandler = moverPokemonEntreTimeEBoxHandler;
    }

    @GetMapping("/jogadores/{id}/time")
    public List<PokemonDoTimeDto> obterTime(@PathVariable String id) {
        return obterTimeAtivoHandler.handle(new ObterTimeAtivoQuery(id));
    }

    @GetMapping("/jogadores/{id}/box")
    public List<PokemonDoTimeDto> obterBox(@PathVariable String id) {
        return obterBoxPokemonHandler.handle(new ObterBoxPokemonQuery(id));
    }

    @PostMapping("/jogadores/{id}/time/definir")
    public void definirTime(@PathVariable String id, @RequestBody DefinirTimeRequest request) {
        definirTimeAtivoHandler.handle(new DefinirTimeAtivoCommand(id, request.pokemonIds()));
    }

    @PostMapping("/jogadores/{id}/box/mover")
    public void mover(@PathVariable String id, @RequestBody MoverPokemonRequest request) {
        moverPokemonEntreTimeEBoxHandler.handle(new MoverPokemonEntreTimeEBoxCommand(id, request.pokemonId(), request.paraBox(), request.slotDestino()));
    }

    public record DefinirTimeRequest(@NotEmpty List<String> pokemonIds) {
    }

    public record MoverPokemonRequest(String pokemonId, boolean paraBox, Integer slotDestino) {
    }
}
