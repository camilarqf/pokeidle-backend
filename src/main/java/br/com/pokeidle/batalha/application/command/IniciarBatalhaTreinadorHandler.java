package br.com.pokeidle.batalha.application.command;

import br.com.pokeidle.batalha.application.query.BatalhaDto;
import br.com.pokeidle.batalha.application.query.BatalhaMapper;
import br.com.pokeidle.batalha.domain.Batalha;
import br.com.pokeidle.batalha.domain.BatalhaOponentePokemon;
import br.com.pokeidle.batalha.domain.BatalhaOponentePokemonRepository;
import br.com.pokeidle.batalha.domain.BatalhaRepository;
import br.com.pokeidle.batalha.domain.StatusBatalha;
import br.com.pokeidle.catalogo.domain.PokemonEspecieRepository;
import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.mundo.domain.NoJornada;
import br.com.pokeidle.mundo.domain.NoJornadaRepository;
import br.com.pokeidle.plantel.application.PlantelService;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.shared.domain.BusinessException;
import br.com.pokeidle.shared.domain.NotFoundException;
import br.com.pokeidle.shared.infrastructure.Ids;
import br.com.pokeidle.treinadores.domain.JogadorTreinadorDerrotadoRepository;
import br.com.pokeidle.treinadores.domain.TreinadorNpc;
import br.com.pokeidle.treinadores.domain.TreinadorNpcPokemonRepository;
import br.com.pokeidle.treinadores.domain.TreinadorNpcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class IniciarBatalhaTreinadorHandler {

    private final JogadorRepository jogadorRepository;
    private final NoJornadaRepository noJornadaRepository;
    private final PlantelService plantelService;
    private final PokemonEspecieRepository pokemonEspecieRepository;
    private final BatalhaRepository batalhaRepository;
    private final BatalhaOponentePokemonRepository batalhaOponentePokemonRepository;
    private final TreinadorNpcRepository treinadorNpcRepository;
    private final TreinadorNpcPokemonRepository treinadorNpcPokemonRepository;
    private final JogadorTreinadorDerrotadoRepository jogadorTreinadorDerrotadoRepository;

    public IniciarBatalhaTreinadorHandler(JogadorRepository jogadorRepository,
                                          NoJornadaRepository noJornadaRepository,
                                          PlantelService plantelService,
                                          PokemonEspecieRepository pokemonEspecieRepository,
                                          BatalhaRepository batalhaRepository,
                                          BatalhaOponentePokemonRepository batalhaOponentePokemonRepository,
                                          TreinadorNpcRepository treinadorNpcRepository,
                                          TreinadorNpcPokemonRepository treinadorNpcPokemonRepository,
                                          JogadorTreinadorDerrotadoRepository jogadorTreinadorDerrotadoRepository) {
        this.jogadorRepository = jogadorRepository;
        this.noJornadaRepository = noJornadaRepository;
        this.plantelService = plantelService;
        this.pokemonEspecieRepository = pokemonEspecieRepository;
        this.batalhaRepository = batalhaRepository;
        this.batalhaOponentePokemonRepository = batalhaOponentePokemonRepository;
        this.treinadorNpcRepository = treinadorNpcRepository;
        this.treinadorNpcPokemonRepository = treinadorNpcPokemonRepository;
        this.jogadorTreinadorDerrotadoRepository = jogadorTreinadorDerrotadoRepository;
    }

    @Transactional
    public BatalhaDto handle(IniciarBatalhaTreinadorCommand command) {
        return iniciar(command.jogadorId(), command.treinadorId(), false);
    }

    @Transactional
    public BatalhaDto handle(IniciarBatalhaLiderCommand command) {
        return iniciar(command.jogadorId(), command.liderId(), true);
    }

    private BatalhaDto iniciar(String jogadorId, Long treinadorId, boolean deveSerLider) {
        Jogador jogador = jogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new NotFoundException("Jogador nao encontrado."));
        NoJornada noAtual = noJornadaRepository.findById(jogador.getNoAtualId())
                .orElseThrow(() -> new NotFoundException("No atual nao encontrado."));
        TreinadorNpc treinador = treinadorNpcRepository.findById(treinadorId)
                .orElseThrow(() -> new NotFoundException("Treinador nao encontrado."));
        if (treinador.isLider() != deveSerLider) {
            throw new BusinessException(deveSerLider ? "O desafio informado nao pertence a um lider." : "O desafio informado nao pertence a um treinador comum.");
        }
        if (!treinador.getNoJornadaId().equals(noAtual.getId())) {
            throw new BusinessException("O treinador nao esta disponivel no no atual.");
        }
        if (batalhaRepository.findFirstByJogadorIdAndStatus(jogador.getId(), StatusBatalha.EM_ANDAMENTO).isPresent()) {
            throw new BusinessException("Ja existe uma batalha em andamento para esse jogador.");
        }
        if (jogadorTreinadorDerrotadoRepository.findByJogadorIdAndTreinadorNpcId(jogador.getId(), treinadorId).isPresent()) {
            throw new BusinessException("Esse treinador ja foi derrotado por este jogador.");
        }

        PokemonCapturado pokemonInicial = plantelService.obterPrimeiroPokemonDisponivelParaBatalha(jogador.getId());
        String batalhaId = Ids.unique();
        List<BatalhaOponentePokemon> equipe = montarEquipeOponente(batalhaId, treinadorId);
        Batalha batalha = Batalha.criarContraTreinador(batalhaId, jogador.getId(), noAtual.getId(), pokemonInicial, treinador, equipe.getFirst());
        batalhaRepository.save(batalha);
        equipe.forEach(batalhaOponentePokemonRepository::save);
        return BatalhaMapper.toDto(batalha);
    }

    private List<BatalhaOponentePokemon> montarEquipeOponente(String batalhaId, Long treinadorId) {
        var definicoes = treinadorNpcPokemonRepository.findByTreinadorNpcIdOrderByOrdemEquipeAsc(treinadorId);
        Map<Long, br.com.pokeidle.catalogo.domain.PokemonEspecie> especies = pokemonEspecieRepository.findAllByIdIn(
                        definicoes.stream().map(def -> def.getEspecieId()).toList())
                .stream()
                .collect(Collectors.toMap(br.com.pokeidle.catalogo.domain.PokemonEspecie::getId, Function.identity()));
        return definicoes.stream()
                .map(def -> BatalhaOponentePokemon.criarDeTreinador(
                        batalhaId,
                        def,
                        java.util.Optional.ofNullable(especies.get(def.getEspecieId()))
                                .orElseThrow(() -> new NotFoundException("Especie do treinador nao encontrada no catalogo local."))
                ))
                .toList();
    }
}
