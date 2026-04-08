package br.com.pokeidle.mundo.api;

import br.com.pokeidle.mundo.application.command.EntrarNoCommand;
import br.com.pokeidle.mundo.application.command.EntrarNoHandler;
import br.com.pokeidle.mundo.application.query.DetalhesNoDto;
import br.com.pokeidle.mundo.application.query.MapaAtualDto;
import br.com.pokeidle.mundo.application.query.ObterDetalhesDoNoHandler;
import br.com.pokeidle.mundo.application.query.ObterDetalhesDoNoQuery;
import br.com.pokeidle.mundo.application.query.ObterMapaAtualHandler;
import br.com.pokeidle.mundo.application.query.ObterMapaAtualQuery;
import br.com.pokeidle.mundo.application.query.ObterProgressoDoNoHandler;
import br.com.pokeidle.mundo.application.query.ObterProgressoDoNoQuery;
import br.com.pokeidle.mundo.application.query.ProgressoDoNoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapaController {

    private final ObterMapaAtualHandler obterMapaAtualHandler;
    private final EntrarNoHandler entrarNoHandler;
    private final ObterDetalhesDoNoHandler obterDetalhesDoNoHandler;
    private final ObterProgressoDoNoHandler obterProgressoDoNoHandler;

    public MapaController(ObterMapaAtualHandler obterMapaAtualHandler,
                          EntrarNoHandler entrarNoHandler,
                          ObterDetalhesDoNoHandler obterDetalhesDoNoHandler,
                          ObterProgressoDoNoHandler obterProgressoDoNoHandler) {
        this.obterMapaAtualHandler = obterMapaAtualHandler;
        this.entrarNoHandler = entrarNoHandler;
        this.obterDetalhesDoNoHandler = obterDetalhesDoNoHandler;
        this.obterProgressoDoNoHandler = obterProgressoDoNoHandler;
    }

    @GetMapping("/jogadores/{id}/mapa")
    public MapaAtualDto obterMapa(@PathVariable String id) {
        return obterMapaAtualHandler.handle(new ObterMapaAtualQuery(id));
    }

    @PostMapping("/jogadores/{id}/entrar-no/{noId}")
    public ResponseEntity<Void> entrarNo(@PathVariable String id, @PathVariable Long noId) {
        entrarNoHandler.handle(new EntrarNoCommand(id, noId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nos/{id}")
    public DetalhesNoDto obterNo(@PathVariable Long id) {
        return obterDetalhesDoNoHandler.handle(new ObterDetalhesDoNoQuery(id));
    }

    @GetMapping("/jogadores/{id}/nos/{noId}/progresso")
    public ProgressoDoNoDto obterProgresso(@PathVariable String id, @PathVariable Long noId) {
        return obterProgressoDoNoHandler.handle(new ObterProgressoDoNoQuery(id, noId));
    }
}
