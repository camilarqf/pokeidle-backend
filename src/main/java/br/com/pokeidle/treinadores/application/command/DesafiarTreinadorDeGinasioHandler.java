package br.com.pokeidle.treinadores.application.command;

import br.com.pokeidle.batalha.application.command.IniciarBatalhaTreinadorCommand;
import br.com.pokeidle.batalha.application.command.IniciarBatalhaTreinadorHandler;
import br.com.pokeidle.batalha.application.query.BatalhaDto;
import org.springframework.stereotype.Service;

@Service
public class DesafiarTreinadorDeGinasioHandler {

    private final IniciarBatalhaTreinadorHandler iniciarBatalhaTreinadorHandler;

    public DesafiarTreinadorDeGinasioHandler(IniciarBatalhaTreinadorHandler iniciarBatalhaTreinadorHandler) {
        this.iniciarBatalhaTreinadorHandler = iniciarBatalhaTreinadorHandler;
    }

    public BatalhaDto handle(DesafiarTreinadorDeGinasioCommand command) {
        return iniciarBatalhaTreinadorHandler.handle(new IniciarBatalhaTreinadorCommand(command.jogadorId(), command.treinadorId()));
    }
}
