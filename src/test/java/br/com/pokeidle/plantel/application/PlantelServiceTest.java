package br.com.pokeidle.plantel.application;

import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.plantel.domain.BoxPokemonRepository;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
import br.com.pokeidle.plantel.domain.TimeAtivoSlot;
import br.com.pokeidle.plantel.domain.TimeAtivoSlotRepository;
import br.com.pokeidle.shared.domain.TipoPokemon;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlantelServiceTest {

    @Test
    void deveColocarPokemonCapturadoNoPrimeiroSlotLivreDoTime() {
        PokemonCapturadoRepository pokemonCapturadoRepository = mock(PokemonCapturadoRepository.class);
        TimeAtivoSlotRepository timeAtivoSlotRepository = mock(TimeAtivoSlotRepository.class);
        BoxPokemonRepository boxPokemonRepository = mock(BoxPokemonRepository.class);
        PlantelService plantelService = new PlantelService(pokemonCapturadoRepository, timeAtivoSlotRepository, boxPokemonRepository);
        PokemonCapturado pokemon = criarPokemon("pk-2", "j-1");

        when(timeAtivoSlotRepository.findByJogadorIdOrderBySlotNumeroAsc("j-1"))
                .thenReturn(List.of(new TimeAtivoSlot("j-1", 1, "pk-1")));
        when(pokemonCapturadoRepository.findById("pk-2")).thenReturn(Optional.of(pokemon));
        when(boxPokemonRepository.findByPokemonCapturadoId("pk-2")).thenReturn(Optional.empty());
        when(timeAtivoSlotRepository.findByJogadorIdAndSlotNumero("j-1", 2)).thenReturn(Optional.empty());
        when(timeAtivoSlotRepository.findByPokemonCapturadoId("pk-2")).thenReturn(Optional.empty());
        when(pokemonCapturadoRepository.findByJogadorIdOrderByCapturadoEmAsc("j-1")).thenReturn(List.of(pokemon));

        plantelService.alocarPokemonCapturado("j-1", "pk-2");

        ArgumentCaptor<TimeAtivoSlot> captor = ArgumentCaptor.forClass(TimeAtivoSlot.class);
        verify(timeAtivoSlotRepository).save(captor.capture());
        assertEquals(2, captor.getValue().getSlotNumero());
        assertEquals("pk-2", captor.getValue().getPokemonCapturadoId());
        verify(boxPokemonRepository, never()).save(any());
    }

    @Test
    void deveEnviarPokemonParaBoxQuandoTimeAtivoJaEstiverCheio() {
        PokemonCapturadoRepository pokemonCapturadoRepository = mock(PokemonCapturadoRepository.class);
        TimeAtivoSlotRepository timeAtivoSlotRepository = mock(TimeAtivoSlotRepository.class);
        BoxPokemonRepository boxPokemonRepository = mock(BoxPokemonRepository.class);
        PlantelService plantelService = new PlantelService(pokemonCapturadoRepository, timeAtivoSlotRepository, boxPokemonRepository);

        when(timeAtivoSlotRepository.findByJogadorIdOrderBySlotNumeroAsc("j-1"))
                .thenReturn(List.of(
                        new TimeAtivoSlot("j-1", 1, "pk-1"),
                        new TimeAtivoSlot("j-1", 2, "pk-2"),
                        new TimeAtivoSlot("j-1", 3, "pk-3")
                ));
        PokemonCapturado pokemon = criarPokemon("pk-4", "j-1");
        when(pokemonCapturadoRepository.findById("pk-4")).thenReturn(Optional.of(pokemon));
        when(boxPokemonRepository.findByPokemonCapturadoId("pk-4")).thenReturn(Optional.empty());
        when(pokemonCapturadoRepository.findByJogadorIdOrderByCapturadoEmAsc("j-1")).thenReturn(List.of(pokemon));

        plantelService.alocarPokemonCapturado("j-1", "pk-4");

        verify(boxPokemonRepository).save(any());
    }

    @Test
    void deveObterPrimeiroPokemonDisponivelRespeitandoOrdemDosSlots() {
        PokemonCapturadoRepository pokemonCapturadoRepository = mock(PokemonCapturadoRepository.class);
        TimeAtivoSlotRepository timeAtivoSlotRepository = mock(TimeAtivoSlotRepository.class);
        BoxPokemonRepository boxPokemonRepository = mock(BoxPokemonRepository.class);
        PlantelService plantelService = new PlantelService(pokemonCapturadoRepository, timeAtivoSlotRepository, boxPokemonRepository);
        PokemonCapturado slotUm = criarPokemon("pk-1", "j-1");
        PokemonCapturado slotDois = criarPokemon("pk-2", "j-1");

        when(timeAtivoSlotRepository.findByJogadorIdOrderBySlotNumeroAsc("j-1"))
                .thenReturn(List.of(
                        new TimeAtivoSlot("j-1", 1, "pk-1"),
                        new TimeAtivoSlot("j-1", 2, "pk-2")
                ));
        when(pokemonCapturadoRepository.findByIdIn(List.of("pk-1", "pk-2")))
                .thenReturn(List.of(slotUm, slotDois));

        PokemonCapturado escolhido = plantelService.obterPrimeiroPokemonDisponivelParaBatalha("j-1");

        assertSame(slotUm, escolhido);
    }

    @Test
    void deveSincronizarRemocoesAntesDeSalvarNovaOrdemDoTime() {
        PokemonCapturadoRepository pokemonCapturadoRepository = mock(PokemonCapturadoRepository.class);
        TimeAtivoSlotRepository timeAtivoSlotRepository = mock(TimeAtivoSlotRepository.class);
        BoxPokemonRepository boxPokemonRepository = mock(BoxPokemonRepository.class);
        PlantelService plantelService = new PlantelService(pokemonCapturadoRepository, timeAtivoSlotRepository, boxPokemonRepository);
        PokemonCapturado pokemonUm = criarPokemon("pk-1", "j-1");
        PokemonCapturado pokemonDois = criarPokemon("pk-2", "j-1");

        when(pokemonCapturadoRepository.findByIdIn(List.of("pk-2", "pk-1")))
                .thenReturn(List.of(pokemonUm, pokemonDois));
        when(timeAtivoSlotRepository.findByJogadorIdOrderBySlotNumeroAsc("j-1"))
                .thenReturn(List.of(
                        new TimeAtivoSlot("j-1", 1, "pk-1"),
                        new TimeAtivoSlot("j-1", 2, "pk-2")
                ));
        when(boxPokemonRepository.findByJogadorIdOrderByArmazenadoEmAsc("j-1")).thenReturn(List.of());
        when(pokemonCapturadoRepository.findByJogadorIdOrderByCapturadoEmAsc("j-1"))
                .thenReturn(List.of(pokemonUm, pokemonDois));

        plantelService.definirTimeAtivo("j-1", List.of("pk-2", "pk-1"));

        verify(timeAtivoSlotRepository).flush();
    }

    private PokemonCapturado criarPokemon(String id, String jogadorId) {
        PokemonEspecie especie = new PokemonEspecie(1L, "bulbasaur", TipoPokemon.GRASS, TipoPokemon.POISON, 45, 49, 49, 45, 7, 69, null, 45, "generation-i");
        return PokemonCapturado.criarInicial(id, jogadorId, especie);
    }
}
