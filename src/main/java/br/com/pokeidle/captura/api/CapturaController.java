package br.com.pokeidle.captura.api;

import br.com.pokeidle.captura.application.command.ResultadoCapturaDto;
import br.com.pokeidle.captura.application.command.TentarCapturaCommand;
import br.com.pokeidle.captura.application.command.TentarCapturaHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CapturaController {

    private final TentarCapturaHandler tentarCapturaHandler;

    public CapturaController(TentarCapturaHandler tentarCapturaHandler) {
        this.tentarCapturaHandler = tentarCapturaHandler;
    }

    @PostMapping("/batalhas/{id}/captura")
    public ResultadoCapturaDto tentarCaptura(@PathVariable String id) {
        return tentarCapturaHandler.handle(new TentarCapturaCommand(id));
    }
}
