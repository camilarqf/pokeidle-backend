package br.com.pokeidle.treinadores.application.event;

import br.com.pokeidle.jogador.domain.BadgeJogador;
import br.com.pokeidle.jogador.domain.BadgeJogadorRepository;
import br.com.pokeidle.jogador.domain.BadgeObtidaDomainEvent;
import br.com.pokeidle.jogador.domain.CodigoBadge;
import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.progresso.application.command.AtualizarProgressoDoNoHandler;
import br.com.pokeidle.progresso.application.command.RegistrarLiderDerrotadoCommand;
import br.com.pokeidle.progresso.application.command.RegistrarTreinadorDerrotadoCommand;
import br.com.pokeidle.shared.application.DomainEventPublisher;
import br.com.pokeidle.shared.domain.NotFoundException;
import br.com.pokeidle.treinadores.domain.Ginasio;
import br.com.pokeidle.treinadores.domain.GinasioRepository;
import br.com.pokeidle.treinadores.domain.JogadorTreinadorDerrotado;
import br.com.pokeidle.treinadores.domain.JogadorTreinadorDerrotadoRepository;
import br.com.pokeidle.treinadores.domain.LiderDeGinasioDerrotadoDomainEvent;
import br.com.pokeidle.treinadores.domain.TreinadorNpcDerrotadoDomainEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class RegistrarVitoriaContraTreinadorHandler {

    private final JogadorTreinadorDerrotadoRepository jogadorTreinadorDerrotadoRepository;
    private final JogadorRepository jogadorRepository;
    private final BadgeJogadorRepository badgeJogadorRepository;
    private final GinasioRepository ginasioRepository;
    private final AtualizarProgressoDoNoHandler atualizarProgressoDoNoHandler;
    private final DomainEventPublisher domainEventPublisher;

    public RegistrarVitoriaContraTreinadorHandler(JogadorTreinadorDerrotadoRepository jogadorTreinadorDerrotadoRepository,
                                                  JogadorRepository jogadorRepository,
                                                  BadgeJogadorRepository badgeJogadorRepository,
                                                  GinasioRepository ginasioRepository,
                                                  AtualizarProgressoDoNoHandler atualizarProgressoDoNoHandler,
                                                  DomainEventPublisher domainEventPublisher) {
        this.jogadorTreinadorDerrotadoRepository = jogadorTreinadorDerrotadoRepository;
        this.jogadorRepository = jogadorRepository;
        this.badgeJogadorRepository = badgeJogadorRepository;
        this.ginasioRepository = ginasioRepository;
        this.atualizarProgressoDoNoHandler = atualizarProgressoDoNoHandler;
        this.domainEventPublisher = domainEventPublisher;
    }

    @EventListener
    @Transactional
    public void on(TreinadorNpcDerrotadoDomainEvent event) {
        if (jogadorTreinadorDerrotadoRepository.findByJogadorIdAndTreinadorNpcId(event.jogadorId(), event.treinadorNpcId()).isPresent()) {
            return;
        }
        jogadorTreinadorDerrotadoRepository.save(new JogadorTreinadorDerrotado(event.jogadorId(), event.treinadorNpcId()));
        Jogador jogador = jogadorRepository.findById(event.jogadorId())
                .orElseThrow(() -> new NotFoundException("Jogador nao encontrado."));
        jogador.creditarMoedas(event.recompensaMoedas());
        jogadorRepository.save(jogador);
        atualizarProgressoDoNoHandler.registrarTreinadorDerrotado(new RegistrarTreinadorDerrotadoCommand(event.jogadorId(), event.treinadorNpcId(), event.noJornadaId()));
    }

    @EventListener
    @Transactional
    public void on(LiderDeGinasioDerrotadoDomainEvent event) {
        if (jogadorTreinadorDerrotadoRepository.findByJogadorIdAndTreinadorNpcId(event.jogadorId(), event.treinadorNpcId()).isPresent()) {
            return;
        }
        jogadorTreinadorDerrotadoRepository.save(new JogadorTreinadorDerrotado(event.jogadorId(), event.treinadorNpcId()));
        Jogador jogador = jogadorRepository.findById(event.jogadorId())
                .orElseThrow(() -> new NotFoundException("Jogador nao encontrado."));
        jogador.creditarMoedas(event.recompensaMoedas());
        jogador.aumentarLevelCap(18);
        jogadorRepository.save(jogador);

        Ginasio ginasio = ginasioRepository.findByNoJornadaId(event.noJornadaId())
                .orElseThrow(() -> new NotFoundException("Ginasio nao encontrado."));
        CodigoBadge codigoBadge = CodigoBadge.valueOf(ginasio.getBadgeCodigo());
        if (badgeJogadorRepository.findByJogadorIdAndCodigo(event.jogadorId(), codigoBadge).isEmpty()) {
            badgeJogadorRepository.save(new BadgeJogador(event.jogadorId(), codigoBadge, ginasio.getBadgeNome()));
            domainEventPublisher.publishAll(List.of(new BadgeObtidaDomainEvent(event.jogadorId(), codigoBadge, ginasio.getBadgeNome())));
        }
        atualizarProgressoDoNoHandler.registrarLiderDerrotado(new RegistrarLiderDerrotadoCommand(event.jogadorId(), event.treinadorNpcId(), ginasio.getId(), event.noJornadaId()));
        domainEventPublisher.publishAll(jogador.pullDomainEvents());
    }
}
