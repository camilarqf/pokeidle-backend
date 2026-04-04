package br.com.pokeidle.batalha.application.command;

import br.com.pokeidle.batalha.domain.Batalha;
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
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
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
    private final PokemonCapturadoRepository pokemonCapturadoRepository;
    private final PokemonEspecieRepository pokemonEspecieRepository;
    private final BatalhaRepository batalhaRepository;
    private final RandomProvider randomProvider;

    public IniciarBatalhaSelvagemHandler(JogadorRepository jogadorRepository,
                                         NoJornadaRepository noJornadaRepository,
                                         PokemonCapturadoRepository pokemonCapturadoRepository,
                                         PokemonEspecieRepository pokemonEspecieRepository,
                                         BatalhaRepository batalhaRepository,
                                         RandomProvider randomProvider) {
        this.jogadorRepository = jogadorRepository;
        this.noJornadaRepository = noJornadaRepository;
        this.pokemonCapturadoRepository = pokemonCapturadoRepository;
        this.pokemonEspecieRepository = pokemonEspecieRepository;
        this.batalhaRepository = batalhaRepository;
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
        PokemonCapturado pokemonAtivo = pokemonCapturadoRepository.findFirstByJogadorIdAndAtivoTrue(jogador.getId())
                .orElseThrow(() -> new BusinessException("O jogador nao possui pokemon ativo."));
        if (pokemonAtivo.estaDerrotado()) {
            throw new BusinessException("O pokemon ativo esta sem HP. Cure o time antes de batalhar.");
        }

        PokemonEspecie selvagem = escolherSelvagem(noAtual);
        int nivelSelvagem = 2 + randomProvider.nextInt(3);
        Batalha batalha = Batalha.criarSelvagem(Ids.unique(), jogador.getId(), noAtual.getId(), pokemonAtivo, selvagem, nivelSelvagem);
        batalhaRepository.save(batalha);
        return BatalhaMapper.toDto(batalha);
    }

    private PokemonEspecie escolherSelvagem(NoJornada noAtual) {
        List<String> nomes = switch (noAtual.getSlug()) {
            case "route-1" -> List.of("pidgey", "rattata");
            default -> throw new BusinessException("Nao existe tabela de encontro configurada para o no " + noAtual.getSlug());
        };
        String escolhido = nomes.get(randomProvider.nextInt(nomes.size()));
        return pokemonEspecieRepository.findByNomeIgnoreCase(escolhido)
                .orElseThrow(() -> new BusinessException("Pokemon " + escolhido + " ainda nao foi importado para o catalogo."));
    }
}
