package br.com.pokeidle.progresso.application.event;

import br.com.pokeidle.batalha.domain.BatalhaSelvagemVencidaDomainEvent;
import br.com.pokeidle.progresso.application.command.AtualizarProgressoDoNoCommand;
import br.com.pokeidle.progresso.application.command.AtualizarProgressoDoNoHandler;
import br.com.pokeidle.progresso.application.command.VerificarDesbloqueioDeNoCommand;
import br.com.pokeidle.progresso.application.command.VerificarDesbloqueioDeNoHandler;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AtualizarProgressoPorVitoriaHandler {

    private final AtualizarProgressoDoNoHandler atualizarProgressoDoNoHandler;
    private final VerificarDesbloqueioDeNoHandler verificarDesbloqueioDeNoHandler;

    public AtualizarProgressoPorVitoriaHandler(AtualizarProgressoDoNoHandler atualizarProgressoDoNoHandler,
                                               VerificarDesbloqueioDeNoHandler verificarDesbloqueioDeNoHandler) {
        this.atualizarProgressoDoNoHandler = atualizarProgressoDoNoHandler;
        this.verificarDesbloqueioDeNoHandler = verificarDesbloqueioDeNoHandler;
    }

    @EventListener
    @Transactional
    public void on(BatalhaSelvagemVencidaDomainEvent event) {
        atualizarProgressoDoNoHandler.handle(new AtualizarProgressoDoNoCommand(event.jogadorId(), event.noId(), 1));
        verificarDesbloqueioDeNoHandler.handle(new VerificarDesbloqueioDeNoCommand(event.jogadorId(), event.noId()));
    }
}
