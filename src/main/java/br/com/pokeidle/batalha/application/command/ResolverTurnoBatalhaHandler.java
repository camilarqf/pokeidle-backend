package br.com.pokeidle.batalha.application.command;

import br.com.pokeidle.batalha.application.query.BatalhaMapper;
import br.com.pokeidle.batalha.application.query.BatalhaDto;
import br.com.pokeidle.batalha.domain.Batalha;
import br.com.pokeidle.batalha.domain.BatalhaRepository;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
import br.com.pokeidle.shared.application.DomainEventPublisher;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResolverTurnoBatalhaHandler {

    private final BatalhaRepository batalhaRepository;
    private final PokemonCapturadoRepository pokemonCapturadoRepository;
    private final DomainEventPublisher domainEventPublisher;

    public ResolverTurnoBatalhaHandler(BatalhaRepository batalhaRepository,
                                       PokemonCapturadoRepository pokemonCapturadoRepository,
                                       DomainEventPublisher domainEventPublisher) {
        this.batalhaRepository = batalhaRepository;
        this.pokemonCapturadoRepository = pokemonCapturadoRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public BatalhaDto handle(ResolverTurnoBatalhaCommand command) {
        Batalha batalha = batalhaRepository.findById(command.batalhaId())
                .orElseThrow(() -> new NotFoundException("Batalha nao encontrada."));
        PokemonCapturado pokemonJogador = pokemonCapturadoRepository.findById(batalha.getPokemonJogadorId())
                .orElseThrow(() -> new NotFoundException("Pokemon do jogador nao encontrado."));
        batalha.resolverTurno(pokemonJogador);
        pokemonCapturadoRepository.save(pokemonJogador);
        batalhaRepository.save(batalha);
        domainEventPublisher.publishAll(batalha.pullDomainEvents());
        return BatalhaMapper.toDto(batalha);
    }
}
