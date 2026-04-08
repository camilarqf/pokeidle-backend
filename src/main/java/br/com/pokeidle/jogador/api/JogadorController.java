package br.com.pokeidle.jogador.api;

import br.com.pokeidle.jogador.application.command.CriarJogadorCommand;
import br.com.pokeidle.jogador.application.command.CriarJogadorHandler;
import br.com.pokeidle.jogador.application.command.EscolherPokemonInicialCommand;
import br.com.pokeidle.jogador.application.command.EscolherPokemonInicialHandler;
import br.com.pokeidle.jogador.application.query.JogadorDto;
import br.com.pokeidle.jogador.application.query.ObterPerfilJogadorHandler;
import br.com.pokeidle.jogador.application.query.ObterPerfilJogadorQuery;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/jogadores")
public class JogadorController {

    private final CriarJogadorHandler criarJogadorHandler;
    private final EscolherPokemonInicialHandler escolherPokemonInicialHandler;
    private final ObterPerfilJogadorHandler obterPerfilJogadorHandler;

    public JogadorController(CriarJogadorHandler criarJogadorHandler,
                             EscolherPokemonInicialHandler escolherPokemonInicialHandler,
                             ObterPerfilJogadorHandler obterPerfilJogadorHandler) {
        this.criarJogadorHandler = criarJogadorHandler;
        this.escolherPokemonInicialHandler = escolherPokemonInicialHandler;
        this.obterPerfilJogadorHandler = obterPerfilJogadorHandler;
    }

    @PostMapping
    public JogadorDto criar(@RequestBody CriarJogadorRequest request) {
        return criarJogadorHandler.handle(new CriarJogadorCommand(request.nomePerfil()));
    }

    @PostMapping("/{id}/pokemon-inicial")
    public JogadorDto escolherPokemonInicial(@PathVariable String id, @RequestBody EscolherPokemonInicialRequest request) {
        return escolherPokemonInicialHandler.handle(new EscolherPokemonInicialCommand(id, request.pokemonEspecieId()));
    }

    @GetMapping("/{id}")
    public JogadorDto obter(@PathVariable String id) {
        return obterPerfilJogadorHandler.handle(new ObterPerfilJogadorQuery(id));
    }

    public record CriarJogadorRequest(@NotBlank String nomePerfil) {
    }

    public record EscolherPokemonInicialRequest(@NotNull Long pokemonEspecieId) {
    }
}
