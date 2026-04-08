package br.com.pokeidle.progresso.application.command;

import br.com.pokeidle.mundo.domain.MissaoNo;
import br.com.pokeidle.mundo.domain.MissaoNoRepository;
import br.com.pokeidle.mundo.domain.ObjetivoMissaoNoRepository;
import br.com.pokeidle.progresso.domain.ProgressoNoJogador;
import br.com.pokeidle.progresso.domain.ProgressoNoJogadorRepository;
import br.com.pokeidle.shared.application.DomainEventPublisher;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtualizarProgressoDoNoHandler {

    private final ProgressoNoJogadorRepository progressoRepository;
    private final MissaoNoRepository missaoNoRepository;
    private final ObjetivoMissaoNoRepository objetivoMissaoNoRepository;
    private final DomainEventPublisher domainEventPublisher;

    public AtualizarProgressoDoNoHandler(ProgressoNoJogadorRepository progressoRepository,
                                         MissaoNoRepository missaoNoRepository,
                                         ObjetivoMissaoNoRepository objetivoMissaoNoRepository,
                                         DomainEventPublisher domainEventPublisher) {
        this.progressoRepository = progressoRepository;
        this.missaoNoRepository = missaoNoRepository;
        this.objetivoMissaoNoRepository = objetivoMissaoNoRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public ProgressoNoJogador handle(AtualizarProgressoDoNoCommand command) {
        ProgressoNoJogador progresso = progressoRepository.findByJogadorIdAndNoJornadaId(command.jogadorId(), command.noId())
                .orElseThrow(() -> new NotFoundException("Progresso do no nao encontrado."));
        MissaoNo missao = missaoNoRepository.findByNoJornadaId(command.noId())
                .orElseThrow(() -> new NotFoundException("Missao do no nao encontrada."));
        progresso.registrarVitoriaSelvagem(command.vitoriasObtidas(), objetivoMissaoNoRepository.findByMissaoNoIdOrderByOrdemAsc(missao.getId()));
        ProgressoNoJogador salvo = progressoRepository.save(progresso);
        domainEventPublisher.publishAll(salvo.pullDomainEvents());
        return salvo;
    }

    @Transactional
    public void registrarEntradaNo(String jogadorId, Long noId) {
        ProgressoNoJogador progresso = progressoRepository.findByJogadorIdAndNoJornadaId(jogadorId, noId)
                .orElseThrow(() -> new NotFoundException("Progresso do no nao encontrado."));
        MissaoNo missao = missaoNoRepository.findByNoJornadaId(noId)
                .orElseThrow(() -> new NotFoundException("Missao do no nao encontrada."));
        progresso.registrarEntradaNo(objetivoMissaoNoRepository.findByMissaoNoIdOrderByOrdemAsc(missao.getId()));
        progressoRepository.save(progresso);
        domainEventPublisher.publishAll(progresso.pullDomainEvents());
    }

    @Transactional
    public void registrarTreinadorDerrotado(RegistrarTreinadorDerrotadoCommand command) {
        ProgressoNoJogador progresso = progressoRepository.findByJogadorIdAndNoJornadaId(command.jogadorId(), command.noId())
                .orElseThrow(() -> new NotFoundException("Progresso do no nao encontrado."));
        MissaoNo missao = missaoNoRepository.findByNoJornadaId(command.noId())
                .orElseThrow(() -> new NotFoundException("Missao do no nao encontrada."));
        progresso.registrarTreinadorDerrotado(objetivoMissaoNoRepository.findByMissaoNoIdOrderByOrdemAsc(missao.getId()));
        progressoRepository.save(progresso);
        domainEventPublisher.publishAll(progresso.pullDomainEvents());
    }

    @Transactional
    public void registrarLiderDerrotado(RegistrarLiderDerrotadoCommand command) {
        ProgressoNoJogador progresso = progressoRepository.findByJogadorIdAndNoJornadaId(command.jogadorId(), command.noId())
                .orElseThrow(() -> new NotFoundException("Progresso do no nao encontrado."));
        MissaoNo missao = missaoNoRepository.findByNoJornadaId(command.noId())
                .orElseThrow(() -> new NotFoundException("Missao do no nao encontrada."));
        progresso.registrarLiderDerrotado(objetivoMissaoNoRepository.findByMissaoNoIdOrderByOrdemAsc(missao.getId()));
        progressoRepository.save(progresso);
        domainEventPublisher.publishAll(progresso.pullDomainEvents());
    }
}
