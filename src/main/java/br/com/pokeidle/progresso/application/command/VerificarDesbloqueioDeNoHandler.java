package br.com.pokeidle.progresso.application.command;

import br.com.pokeidle.mundo.domain.NoConexao;
import br.com.pokeidle.mundo.domain.NoConexaoRepository;
import br.com.pokeidle.progresso.domain.NoDesbloqueadoDomainEvent;
import br.com.pokeidle.progresso.domain.ProgressoNoJogador;
import br.com.pokeidle.progresso.domain.ProgressoNoJogadorRepository;
import br.com.pokeidle.shared.application.DomainEventPublisher;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VerificarDesbloqueioDeNoHandler {

    private final ProgressoNoJogadorRepository progressoRepository;
    private final NoConexaoRepository noConexaoRepository;
    private final DomainEventPublisher domainEventPublisher;

    public VerificarDesbloqueioDeNoHandler(ProgressoNoJogadorRepository progressoRepository,
                                           NoConexaoRepository noConexaoRepository,
                                           DomainEventPublisher domainEventPublisher) {
        this.progressoRepository = progressoRepository;
        this.noConexaoRepository = noConexaoRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public void handle(VerificarDesbloqueioDeNoCommand command) {
        ProgressoNoJogador progressoOrigem = progressoRepository.findByJogadorIdAndNoJornadaId(command.jogadorId(), command.noIdOrigem())
                .orElseThrow(() -> new NotFoundException("Progresso do no origem nao encontrado."));
        if (!progressoOrigem.isConcluido()) {
            return;
        }

        for (NoConexao conexao : noConexaoRepository.findByOrigemNoId(command.noIdOrigem())) {
            progressoRepository.findByJogadorIdAndNoJornadaId(command.jogadorId(), conexao.getDestinoNoId())
                    .ifPresent(destino -> {
                        if (destino.desbloquear()) {
                            progressoRepository.save(destino);
                            domainEventPublisher.publishAll(List.of(new NoDesbloqueadoDomainEvent(command.jogadorId(), destino.getNoJornadaId())));
                        }
                    });
        }
    }
}
