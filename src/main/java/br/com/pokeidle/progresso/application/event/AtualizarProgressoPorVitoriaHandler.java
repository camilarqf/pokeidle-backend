package br.com.pokeidle.progresso.application.event;

import br.com.pokeidle.batalha.domain.BatalhaSelvagemVencidaDomainEvent;
import br.com.pokeidle.progresso.application.command.AtualizarProgressoDoNoCommand;
import br.com.pokeidle.progresso.application.command.AtualizarProgressoDoNoHandler;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AtualizarProgressoPorVitoriaHandler {

    private final AtualizarProgressoDoNoHandler atualizarProgressoDoNoHandler;

    public AtualizarProgressoPorVitoriaHandler(AtualizarProgressoDoNoHandler atualizarProgressoDoNoHandler) {
        this.atualizarProgressoDoNoHandler = atualizarProgressoDoNoHandler;
    }

    @EventListener
    @Transactional
    public void on(BatalhaSelvagemVencidaDomainEvent event) {
        atualizarProgressoDoNoHandler.handle(new AtualizarProgressoDoNoCommand(event.jogadorId(), event.noId(), 1));
    }
}
