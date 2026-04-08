package br.com.pokeidle.treinadores.api;

import br.com.pokeidle.treinadores.application.query.ObterTreinadoresDoNoHandler;
import br.com.pokeidle.treinadores.application.query.ObterTreinadoresDoNoQuery;
import br.com.pokeidle.treinadores.application.query.TreinadorDoNoDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TreinadorController {

    private final ObterTreinadoresDoNoHandler obterTreinadoresDoNoHandler;

    public TreinadorController(ObterTreinadoresDoNoHandler obterTreinadoresDoNoHandler) {
        this.obterTreinadoresDoNoHandler = obterTreinadoresDoNoHandler;
    }

    @GetMapping("/nos/{noId}/treinadores")
    public List<TreinadorDoNoDto> obterPorNo(@PathVariable Long noId) {
        return obterTreinadoresDoNoHandler.handle(new ObterTreinadoresDoNoQuery(noId));
    }
}
