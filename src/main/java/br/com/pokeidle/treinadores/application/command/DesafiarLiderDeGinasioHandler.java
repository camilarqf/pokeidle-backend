package br.com.pokeidle.treinadores.application.command;

import br.com.pokeidle.batalha.application.command.IniciarBatalhaLiderCommand;
import br.com.pokeidle.batalha.application.command.IniciarBatalhaTreinadorHandler;
import br.com.pokeidle.batalha.application.query.BatalhaDto;
import org.springframework.stereotype.Service;

@Service
public class DesafiarLiderDeGinasioHandler {

    private final IniciarBatalhaTreinadorHandler iniciarBatalhaTreinadorHandler;

    public DesafiarLiderDeGinasioHandler(IniciarBatalhaTreinadorHandler iniciarBatalhaTreinadorHandler) {
        this.iniciarBatalhaTreinadorHandler = iniciarBatalhaTreinadorHandler;
    }

    public BatalhaDto handle(DesafiarLiderDeGinasioCommand command) {
        return iniciarBatalhaTreinadorHandler.handle(new IniciarBatalhaLiderCommand(command.jogadorId(), command.liderId()));
    }
}
