package br.com.pokeidle.progresso.application.event;

import br.com.pokeidle.progresso.application.command.VerificarDesbloqueioDeNoCommand;
import br.com.pokeidle.progresso.application.command.VerificarDesbloqueioDeNoHandler;
import br.com.pokeidle.progresso.domain.MissaoDeNoConcluidaDomainEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DesbloquearNosAoConcluirMissaoHandler {

    private final VerificarDesbloqueioDeNoHandler verificarDesbloqueioDeNoHandler;

    public DesbloquearNosAoConcluirMissaoHandler(VerificarDesbloqueioDeNoHandler verificarDesbloqueioDeNoHandler) {
        this.verificarDesbloqueioDeNoHandler = verificarDesbloqueioDeNoHandler;
    }

    @EventListener
    @Transactional
    public void on(MissaoDeNoConcluidaDomainEvent event) {
        verificarDesbloqueioDeNoHandler.handle(new VerificarDesbloqueioDeNoCommand(event.jogadorId(), event.noJornadaId()));
    }
}
