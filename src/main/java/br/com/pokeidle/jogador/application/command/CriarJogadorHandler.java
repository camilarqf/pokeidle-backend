package br.com.pokeidle.jogador.application.command;

import br.com.pokeidle.jogador.application.query.JogadorDto;
import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.mundo.domain.NoJornadaRepository;
import br.com.pokeidle.shared.application.DomainEventPublisher;
import br.com.pokeidle.shared.domain.BusinessException;
import br.com.pokeidle.shared.infrastructure.Ids;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriarJogadorHandler {

    private final JogadorRepository jogadorRepository;
    private final NoJornadaRepository noJornadaRepository;
    private final DomainEventPublisher domainEventPublisher;

    public CriarJogadorHandler(JogadorRepository jogadorRepository,
                               NoJornadaRepository noJornadaRepository,
                               DomainEventPublisher domainEventPublisher) {
        this.jogadorRepository = jogadorRepository;
        this.noJornadaRepository = noJornadaRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public JogadorDto handle(CriarJogadorCommand command) {
        if (jogadorRepository.existsByNomePerfilIgnoreCase(command.nomePerfil())) {
            throw new BusinessException("Ja existe um jogador com esse nome.");
        }
        Long noInicialId = noJornadaRepository.findAllByOrderByOrdemMapaAsc().stream()
                .filter(no -> no.isDesbloqueadoInicial() && no.ehCidade())
                .findFirst()
                .orElseThrow(() -> new BusinessException("Nao ha no inicial configurado."))
                .getId();
        Jogador jogador = Jogador.criar(Ids.unique(), command.nomePerfil(), noInicialId);
        jogadorRepository.save(jogador);
        domainEventPublisher.publishAll(jogador.pullDomainEvents());
        return new JogadorDto(jogador.getId(), jogador.getNomePerfil(), jogador.getSaldoMoedas(), jogador.getNoAtualId(), jogador.isPokemonInicialEscolhido(), jogador.getNivelCapAtual(), java.util.List.of());
    }
}
