package br.com.pokeidle.captura.application.command;

import br.com.pokeidle.batalha.domain.Batalha;
import br.com.pokeidle.batalha.domain.BatalhaRepository;
import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.catalogo.domain.PokemonEspecieRepository;
import br.com.pokeidle.captura.domain.CalculadoraCaptura;
import br.com.pokeidle.inventario.domain.CodigoItem;
import br.com.pokeidle.inventario.domain.InventarioJogador;
import br.com.pokeidle.inventario.domain.InventarioJogadorRepository;
import br.com.pokeidle.inventario.domain.Item;
import br.com.pokeidle.inventario.domain.ItemRepository;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
import br.com.pokeidle.shared.application.DomainEventPublisher;
import br.com.pokeidle.shared.domain.BusinessException;
import br.com.pokeidle.shared.domain.NotFoundException;
import br.com.pokeidle.shared.domain.RandomProvider;
import br.com.pokeidle.shared.infrastructure.Ids;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TentarCapturaHandler {

    private final BatalhaRepository batalhaRepository;
    private final PokemonEspecieRepository pokemonEspecieRepository;
    private final PokemonCapturadoRepository pokemonCapturadoRepository;
    private final ItemRepository itemRepository;
    private final InventarioJogadorRepository inventarioJogadorRepository;
    private final RandomProvider randomProvider;
    private final DomainEventPublisher domainEventPublisher;

    public TentarCapturaHandler(BatalhaRepository batalhaRepository,
                                PokemonEspecieRepository pokemonEspecieRepository,
                                PokemonCapturadoRepository pokemonCapturadoRepository,
                                ItemRepository itemRepository,
                                InventarioJogadorRepository inventarioJogadorRepository,
                                RandomProvider randomProvider,
                                DomainEventPublisher domainEventPublisher) {
        this.batalhaRepository = batalhaRepository;
        this.pokemonEspecieRepository = pokemonEspecieRepository;
        this.pokemonCapturadoRepository = pokemonCapturadoRepository;
        this.itemRepository = itemRepository;
        this.inventarioJogadorRepository = inventarioJogadorRepository;
        this.randomProvider = randomProvider;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public ResultadoCapturaDto handle(TentarCapturaCommand command) {
        Batalha batalha = batalhaRepository.findById(command.batalhaId())
                .orElseThrow(() -> new NotFoundException("Batalha nao encontrada."));
        if (!batalha.estaEmAndamento()) {
            throw new BusinessException("A captura so pode ser tentada durante uma batalha ativa.");
        }
        Item pokeBall = itemRepository.findByCodigo(CodigoItem.POKE_BALL)
                .orElseThrow(() -> new NotFoundException("Pokeball nao cadastrada."));
        InventarioJogador slot = inventarioJogadorRepository.findByJogadorIdAndItemId(batalha.getJogadorId(), pokeBall.getId())
                .orElseThrow(() -> new BusinessException("O jogador nao possui Pokeball."));
        slot.remover(1);
        inventarioJogadorRepository.save(slot);

        PokemonEspecie especie = pokemonEspecieRepository.findById(batalha.getEspecieSelvagemId())
                .orElseThrow(() -> new NotFoundException("Especie selvagem nao encontrada."));
        double chance = CalculadoraCaptura.chance(batalha.getHpAtualSelvagem(), batalha.getHpMaximoSelvagem(), especie.getTaxaCaptura());
        boolean capturado = randomProvider.nextDouble() <= chance;
        if (!capturado) {
            return new ResultadoCapturaDto(false, chance, null, batalha.getStatus().name());
        }

        PokemonCapturado pokemonCapturado = PokemonCapturado.criarSelvagemCapturado(Ids.unique(), batalha.getJogadorId(), especie, batalha.getNivelSelvagem());
        pokemonCapturadoRepository.save(pokemonCapturado);
        batalha.registrarCaptura(pokemonCapturado.getId());
        batalhaRepository.save(batalha);
        domainEventPublisher.publishAll(batalha.pullDomainEvents());
        return new ResultadoCapturaDto(true, chance, pokemonCapturado.getId(), batalha.getStatus().name());
    }
}
