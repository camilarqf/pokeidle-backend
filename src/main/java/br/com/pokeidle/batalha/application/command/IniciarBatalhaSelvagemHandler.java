package br.com.pokeidle.batalha.application.command;

import br.com.pokeidle.batalha.domain.Batalha;
import br.com.pokeidle.batalha.domain.BatalhaOponentePokemon;
import br.com.pokeidle.batalha.domain.BatalhaOponentePokemonRepository;
import br.com.pokeidle.batalha.domain.BatalhaRepository;
import br.com.pokeidle.batalha.domain.StatusBatalha;
import br.com.pokeidle.batalha.application.query.BatalhaMapper;
import br.com.pokeidle.batalha.application.query.BatalhaDto;
import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.catalogo.domain.PokemonEspecieRepository;
import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.mundo.domain.NoJornada;
import br.com.pokeidle.mundo.domain.NoJornadaRepository;
import br.com.pokeidle.plantel.application.PlantelService;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.shared.domain.BusinessException;
import br.com.pokeidle.shared.domain.NotFoundException;
import br.com.pokeidle.shared.domain.RandomProvider;
import br.com.pokeidle.shared.infrastructure.Ids;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IniciarBatalhaSelvagemHandler {

    private final JogadorRepository jogadorRepository;
    private final NoJornadaRepository noJornadaRepository;
    private final PlantelService plantelService;
    private final PokemonEspecieRepository pokemonEspecieRepository;
    private final BatalhaRepository batalhaRepository;
    private final BatalhaOponentePokemonRepository batalhaOponentePokemonRepository;
    private final RandomProvider randomProvider;

    public IniciarBatalhaSelvagemHandler(JogadorRepository jogadorRepository,
                                         NoJornadaRepository noJornadaRepository,
                                         PlantelService plantelService,
                                         PokemonEspecieRepository pokemonEspecieRepository,
                                         BatalhaRepository batalhaRepository,
                                         BatalhaOponentePokemonRepository batalhaOponentePokemonRepository,
                                         RandomProvider randomProvider) {
        this.jogadorRepository = jogadorRepository;
        this.noJornadaRepository = noJornadaRepository;
        this.plantelService = plantelService;
        this.pokemonEspecieRepository = pokemonEspecieRepository;
        this.batalhaRepository = batalhaRepository;
        this.batalhaOponentePokemonRepository = batalhaOponentePokemonRepository;
        this.randomProvider = randomProvider;
    }

    @Transactional
    public BatalhaDto handle(IniciarBatalhaSelvagemCommand command) {
        Jogador jogador = jogadorRepository.findById(command.jogadorId())
                .orElseThrow(() -> new NotFoundException("Jogador nao encontrado."));
        NoJornada noAtual = noJornadaRepository.findById(jogador.getNoAtualId())
                .orElseThrow(() -> new NotFoundException("No atual nao encontrado."));
        if (!noAtual.isPermiteBatalhaSelvagem()) {
            throw new BusinessException("O no atual nao permite batalha selvagem.");
        }
        if (batalhaRepository.findFirstByJogadorIdAndStatus(jogador.getId(), StatusBatalha.EM_ANDAMENTO).isPresent()) {
            throw new BusinessException("Ja existe uma batalha em andamento para esse jogador.");
        }
        PokemonCapturado pokemonAtivo = plantelService.obterPrimeiroPokemonDisponivelParaBatalha(jogador.getId());

        PokemonEspecie selvagem = escolherSelvagem(noAtual);
        int nivelSelvagem = 2 + randomProvider.nextInt(3);
        String batalhaId = Ids.unique();
        Batalha batalha = Batalha.criarSelvagem(batalhaId, jogador.getId(), noAtual.getId(), pokemonAtivo, selvagem, nivelSelvagem);
        batalhaRepository.save(batalha);
        batalhaOponentePokemonRepository.save(BatalhaOponentePokemon.criarSelvagem(batalhaId, selvagem, nivelSelvagem));
        return BatalhaMapper.toDto(batalha);
    }

    private PokemonEspecie escolherSelvagem(NoJornada noAtual) {
        List<String> nomes = switch (noAtual.getSlug()) {
            case "route-1" -> List.of("pidgey", "rattata");
            case "viridian-forest" -> List.of("caterpie", "weedle", "pidgey", "pikachu");
            case "pewter-city" -> List.of("geodude", "sandshrew");
            default -> throw new BusinessException("Nao existe tabela de encontro configurada para o no " + noAtual.getSlug());
        };
        String escolhido = nomes.get(randomProvider.nextInt(nomes.size()));
        return pokemonEspecieRepository.findByNomeIgnoreCase(escolhido)
                .orElseThrow(() -> new BusinessException("Pokemon " + escolhido + " ainda nao foi importado para o catalogo."));
    }
}
