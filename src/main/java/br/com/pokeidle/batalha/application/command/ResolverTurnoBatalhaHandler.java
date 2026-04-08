package br.com.pokeidle.batalha.application.command;

import br.com.pokeidle.batalha.application.query.BatalhaDto;
import br.com.pokeidle.batalha.application.query.BatalhaMapper;
import br.com.pokeidle.batalha.domain.Batalha;
import br.com.pokeidle.batalha.domain.BatalhaOponentePokemon;
import br.com.pokeidle.batalha.domain.BatalhaOponentePokemonRepository;
import br.com.pokeidle.batalha.domain.BatalhaRepository;
import br.com.pokeidle.batalha.domain.TipoBatalha;
import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.plantel.application.PlantelService;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
import br.com.pokeidle.shared.application.DomainEventPublisher;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResolverTurnoBatalhaHandler {

    private final BatalhaRepository batalhaRepository;
    private final BatalhaOponentePokemonRepository batalhaOponentePokemonRepository;
    private final PokemonCapturadoRepository pokemonCapturadoRepository;
    private final JogadorRepository jogadorRepository;
    private final PlantelService plantelService;
    private final DomainEventPublisher domainEventPublisher;

    public ResolverTurnoBatalhaHandler(BatalhaRepository batalhaRepository,
                                       BatalhaOponentePokemonRepository batalhaOponentePokemonRepository,
                                       PokemonCapturadoRepository pokemonCapturadoRepository,
                                       JogadorRepository jogadorRepository,
                                       PlantelService plantelService,
                                       DomainEventPublisher domainEventPublisher) {
        this.batalhaRepository = batalhaRepository;
        this.batalhaOponentePokemonRepository = batalhaOponentePokemonRepository;
        this.pokemonCapturadoRepository = pokemonCapturadoRepository;
        this.jogadorRepository = jogadorRepository;
        this.plantelService = plantelService;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public BatalhaDto handle(ResolverTurnoBatalhaCommand command) {
        Batalha batalha = batalhaRepository.findById(command.batalhaId())
                .orElseThrow(() -> new NotFoundException("Batalha nao encontrada."));
        Jogador jogador = jogadorRepository.findById(batalha.getJogadorId())
                .orElseThrow(() -> new NotFoundException("Jogador nao encontrado."));
        PokemonCapturado pokemonJogador = pokemonCapturadoRepository.findById(batalha.getPokemonJogadorId())
                .orElseThrow(() -> new NotFoundException("Pokemon do jogador nao encontrado."));
        BatalhaOponentePokemon oponenteAtual = carregarOponenteAtual(batalha);
        if (oponenteAtual == null) {
            batalha.registrarDerrota();
            batalhaRepository.save(batalha);
            return BatalhaMapper.toDto(batalha);
        }

        batalha.resolverTurno(pokemonJogador, oponenteAtual, jogador.getNivelCapAtual());
        pokemonCapturadoRepository.save(pokemonJogador);
        batalhaOponentePokemonRepository.save(oponenteAtual);

        if (pokemonJogador.estaDerrotado() && batalha.estaEmAndamento()) {
            plantelService.obterTimeAtivo(jogador.getId()).stream()
                    .filter(pokemon -> !pokemon.estaDerrotado())
                    .filter(pokemon -> !pokemon.getId().equals(pokemonJogador.getId()))
                    .findFirst()
                    .ifPresentOrElse(
                            proximo -> batalha.trocarPokemonJogador(proximo.getId()),
                            batalha::registrarDerrota
                    );
        }

        if (batalha.estaEmAndamento() && oponenteAtual.isDerrotado()) {
            if (batalha.getTipo() == TipoBatalha.SELVAGEM) {
                batalha.registrarVitoriaFinal();
            } else {
                batalhaOponentePokemonRepository.findByBatalhaIdOrderByOrdemEquipeAsc(batalha.getId()).stream()
                        .filter(oponente -> !oponente.isDerrotado())
                        .findFirst()
                        .ifPresentOrElse(
                                batalha::avancarOponente,
                                batalha::registrarVitoriaFinal
                        );
            }
        }

        batalhaRepository.save(batalha);
        domainEventPublisher.publishAll(batalha.pullDomainEvents());
        return BatalhaMapper.toDto(batalha);
    }

    private BatalhaOponentePokemon carregarOponenteAtual(Batalha batalha) {
        var oponenteAtual = batalhaOponentePokemonRepository
                .findByBatalhaIdAndOrdemEquipe(batalha.getId(), batalha.getIndiceOponenteAtual());
        if (oponenteAtual.isPresent()) {
            return oponenteAtual.get();
        }

        if (batalha.getTipo() == TipoBatalha.SELVAGEM) {
            return null;
        }

        return batalhaOponentePokemonRepository.findByBatalhaIdOrderByOrdemEquipeAsc(batalha.getId()).stream()
                .filter(oponente -> !oponente.isDerrotado())
                .findFirst()
                .map(oponente -> {
                    batalha.avancarOponente(oponente);
                    return oponente;
                })
                .orElse(null);
    }
}
