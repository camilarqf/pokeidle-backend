package br.com.pokeidle.plantel.application;

import br.com.pokeidle.plantel.domain.BoxPokemon;
import br.com.pokeidle.plantel.domain.BoxPokemonRepository;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
import br.com.pokeidle.plantel.domain.TimeAtivoSlot;
import br.com.pokeidle.plantel.domain.TimeAtivoSlotRepository;
import br.com.pokeidle.shared.domain.BusinessException;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PlantelService {

    public static final int TAMANHO_TIME_ATIVO = 3;

    private final PokemonCapturadoRepository pokemonCapturadoRepository;
    private final TimeAtivoSlotRepository timeAtivoSlotRepository;
    private final BoxPokemonRepository boxPokemonRepository;

    public PlantelService(PokemonCapturadoRepository pokemonCapturadoRepository,
                          TimeAtivoSlotRepository timeAtivoSlotRepository,
                          BoxPokemonRepository boxPokemonRepository) {
        this.pokemonCapturadoRepository = pokemonCapturadoRepository;
        this.timeAtivoSlotRepository = timeAtivoSlotRepository;
        this.boxPokemonRepository = boxPokemonRepository;
    }

    @Transactional
    public void alocarPokemonCapturado(String jogadorId, String pokemonCapturadoId) {
        int slotLivre = encontrarPrimeiroSlotLivre(jogadorId);
        if (slotLivre > 0) {
            inserirNoSlot(jogadorId, pokemonCapturadoId, slotLivre);
            return;
        }
        moverParaBox(jogadorId, pokemonCapturadoId);
    }

    @Transactional
    public void definirTimeAtivo(String jogadorId, List<String> pokemonIdsOrdenados) {
        if (pokemonIdsOrdenados.isEmpty() || pokemonIdsOrdenados.size() > TAMANHO_TIME_ATIVO) {
            throw new BusinessException("O time ativo deve ter entre 1 e 3 pokemons.");
        }

        List<PokemonCapturado> pokemons = pokemonCapturadoRepository.findByIdIn(pokemonIdsOrdenados);
        if (pokemons.size() != pokemonIdsOrdenados.size() || pokemons.stream().anyMatch(pokemon -> !pokemon.getJogadorId().equals(jogadorId))) {
            throw new BusinessException("Todos os pokemons do time devem pertencer ao jogador.");
        }

        timeAtivoSlotRepository.findByJogadorIdOrderBySlotNumeroAsc(jogadorId).forEach(timeAtivoSlotRepository::delete);
        timeAtivoSlotRepository.flush();
        boxPokemonRepository.findByJogadorIdOrderByArmazenadoEmAsc(jogadorId).stream()
                .filter(box -> pokemonIdsOrdenados.contains(box.getPokemonCapturadoId()))
                .forEach(boxPokemonRepository::delete);

        for (int i = 0; i < pokemonIdsOrdenados.size(); i++) {
            timeAtivoSlotRepository.save(new TimeAtivoSlot(jogadorId, i + 1, pokemonIdsOrdenados.get(i)));
        }

        List<String> idsNoTime = pokemonIdsOrdenados;
        pokemonCapturadoRepository.findByJogadorIdOrderByCapturadoEmAsc(jogadorId)
                .forEach(pokemon -> {
                    pokemon.definirAtivo(idsNoTime.contains(pokemon.getId()));
                    pokemonCapturadoRepository.save(pokemon);
                    if (!idsNoTime.contains(pokemon.getId()) && boxPokemonRepository.findByPokemonCapturadoId(pokemon.getId()).isEmpty()) {
                        boxPokemonRepository.save(new BoxPokemon(jogadorId, pokemon.getId()));
                    }
                });
    }

    @Transactional
    public void moverEntreTimeEBox(String jogadorId, String pokemonId, boolean paraBox, Integer slotDestino) {
        if (paraBox) {
            moverParaBox(jogadorId, pokemonId);
            return;
        }
        if (slotDestino == null || slotDestino < 1 || slotDestino > TAMANHO_TIME_ATIVO) {
            throw new BusinessException("Informe um slot de destino entre 1 e 3.");
        }
        inserirNoSlot(jogadorId, pokemonId, slotDestino);
    }

    public List<PokemonCapturado> obterTimeAtivo(String jogadorId) {
        List<TimeAtivoSlot> slots = timeAtivoSlotRepository.findByJogadorIdOrderBySlotNumeroAsc(jogadorId);
        Map<String, PokemonCapturado> porId = pokemonCapturadoRepository.findByIdIn(
                        slots.stream().map(TimeAtivoSlot::getPokemonCapturadoId).toList())
                .stream()
                .collect(Collectors.toMap(PokemonCapturado::getId, Function.identity()));
        return slots.stream()
                .map(slot -> porId.get(slot.getPokemonCapturadoId()))
                .filter(pokemon -> pokemon != null)
                .sorted(Comparator.comparingInt(pokemon -> slots.stream()
                        .filter(slot -> slot.getPokemonCapturadoId().equals(pokemon.getId()))
                        .findFirst()
                        .map(TimeAtivoSlot::getSlotNumero)
                        .orElse(TAMANHO_TIME_ATIVO)))
                .toList();
    }

    public List<PokemonCapturado> obterBox(String jogadorId) {
        List<BoxPokemon> box = boxPokemonRepository.findByJogadorIdOrderByArmazenadoEmAsc(jogadorId);
        Map<String, PokemonCapturado> porId = pokemonCapturadoRepository.findByIdIn(
                        box.stream().map(BoxPokemon::getPokemonCapturadoId).toList())
                .stream()
                .collect(Collectors.toMap(PokemonCapturado::getId, Function.identity()));
        return box.stream()
                .map(slot -> porId.get(slot.getPokemonCapturadoId()))
                .filter(pokemon -> pokemon != null)
                .toList();
    }

    public PokemonCapturado obterPrimeiroPokemonDisponivelParaBatalha(String jogadorId) {
        return obterTimeAtivo(jogadorId).stream()
                .filter(pokemon -> !pokemon.estaDerrotado())
                .findFirst()
                .orElseThrow(() -> new BusinessException("O time ativo nao possui pokemons com HP."));
    }

    private void inserirNoSlot(String jogadorId, String pokemonId, int slotDestino) {
        PokemonCapturado pokemon = pokemonCapturadoRepository.findById(pokemonId)
                .filter(encontrado -> encontrado.getJogadorId().equals(jogadorId))
                .orElseThrow(() -> new NotFoundException("Pokemon do jogador nao encontrado."));

        boxPokemonRepository.findByPokemonCapturadoId(pokemonId).ifPresent(boxPokemonRepository::delete);

        timeAtivoSlotRepository.findByJogadorIdAndSlotNumero(jogadorId, slotDestino)
                .ifPresent(slotOcupado -> {
                    if (!slotOcupado.getPokemonCapturadoId().equals(pokemonId)) {
                        moverParaBox(jogadorId, slotOcupado.getPokemonCapturadoId());
                        timeAtivoSlotRepository.delete(slotOcupado);
                    }
                });

        timeAtivoSlotRepository.findByPokemonCapturadoId(pokemonId)
                .ifPresent(timeAtivoSlotRepository::delete);

        timeAtivoSlotRepository.save(new TimeAtivoSlot(jogadorId, slotDestino, pokemonId));
        sincronizarEstadoAtivo(jogadorId);
    }

    private void moverParaBox(String jogadorId, String pokemonId) {
        PokemonCapturado pokemon = pokemonCapturadoRepository.findById(pokemonId)
                .filter(encontrado -> encontrado.getJogadorId().equals(jogadorId))
                .orElseThrow(() -> new NotFoundException("Pokemon do jogador nao encontrado."));

        List<TimeAtivoSlot> slots = timeAtivoSlotRepository.findByJogadorIdOrderBySlotNumeroAsc(jogadorId);
        if (slots.size() <= 1 && slots.stream().anyMatch(slot -> slot.getPokemonCapturadoId().equals(pokemonId))) {
            throw new BusinessException("O jogador precisa manter ao menos um pokemon no time ativo.");
        }

        timeAtivoSlotRepository.findByPokemonCapturadoId(pokemonId).ifPresent(timeAtivoSlotRepository::delete);
        if (boxPokemonRepository.findByPokemonCapturadoId(pokemonId).isEmpty()) {
            boxPokemonRepository.save(new BoxPokemon(jogadorId, pokemonId));
        }
        pokemon.definirAtivo(false);
        pokemonCapturadoRepository.save(pokemon);
        sincronizarEstadoAtivo(jogadorId);
    }

    private void sincronizarEstadoAtivo(String jogadorId) {
        List<String> idsNoTime = timeAtivoSlotRepository.findByJogadorIdOrderBySlotNumeroAsc(jogadorId).stream()
                .map(TimeAtivoSlot::getPokemonCapturadoId)
                .toList();

        pokemonCapturadoRepository.findByJogadorIdOrderByCapturadoEmAsc(jogadorId)
                .forEach(pokemon -> {
                    pokemon.definirAtivo(idsNoTime.contains(pokemon.getId()));
                    pokemonCapturadoRepository.save(pokemon);
                });
    }

    private int encontrarPrimeiroSlotLivre(String jogadorId) {
        List<Integer> ocupados = timeAtivoSlotRepository.findByJogadorIdOrderBySlotNumeroAsc(jogadorId).stream()
                .map(TimeAtivoSlot::getSlotNumero)
                .toList();
        for (int i = 1; i <= TAMANHO_TIME_ATIVO; i++) {
            if (!ocupados.contains(i)) {
                return i;
            }
        }
        return -1;
    }
}
