package br.com.pokeidle.batalha.application.command;

import br.com.pokeidle.batalha.domain.Batalha;
import br.com.pokeidle.batalha.domain.BatalhaOponentePokemon;
import br.com.pokeidle.batalha.domain.BatalhaOponentePokemonRepository;
import br.com.pokeidle.batalha.domain.BatalhaRepository;
import br.com.pokeidle.batalha.domain.StatusBatalha;
import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.catalogo.domain.PokemonEspecieRepository;
import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.mundo.domain.NoJornada;
import br.com.pokeidle.mundo.domain.NoJornadaRepository;
import br.com.pokeidle.mundo.domain.TipoNo;
import br.com.pokeidle.plantel.application.PlantelService;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.shared.domain.RandomProvider;
import br.com.pokeidle.shared.domain.TipoPokemon;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IniciarBatalhaSelvagemHandlerTest {

    @Test
    void deveIniciarBatalhaComPokemonDoPrimeiroSlotDisponivel() {
        JogadorRepository jogadorRepository = mock(JogadorRepository.class);
        NoJornadaRepository noJornadaRepository = mock(NoJornadaRepository.class);
        PlantelService plantelService = mock(PlantelService.class);
        PokemonEspecieRepository pokemonEspecieRepository = mock(PokemonEspecieRepository.class);
        BatalhaRepository batalhaRepository = mock(BatalhaRepository.class);
        BatalhaOponentePokemonRepository batalhaOponentePokemonRepository = mock(BatalhaOponentePokemonRepository.class);
        RandomProvider randomProvider = mock(RandomProvider.class);
        IniciarBatalhaSelvagemHandler handler = new IniciarBatalhaSelvagemHandler(
                jogadorRepository,
                noJornadaRepository,
                plantelService,
                pokemonEspecieRepository,
                batalhaRepository,
                batalhaOponentePokemonRepository,
                randomProvider
        );

        Jogador jogador = Jogador.criar("j-1", "Red", 101L);
        NoJornada route1 = new NoJornada(101L, 1L, "Route 1", "route-1", TipoNo.ROTA, "Primeira rota", 2, true, true, false);
        PokemonCapturado pidgey = criarPokemon("pk-slot-1", "j-1", 16L, "pidgey", TipoPokemon.NORMAL, TipoPokemon.FLYING);
        PokemonEspecie rattata = new PokemonEspecie(19L, "rattata", TipoPokemon.NORMAL, null, 30, 56, 35, 72, 3, 35, null, 255, "generation-i");

        when(jogadorRepository.findById("j-1")).thenReturn(Optional.of(jogador));
        when(noJornadaRepository.findById(101L)).thenReturn(Optional.of(route1));
        when(batalhaRepository.findFirstByJogadorIdAndStatus("j-1", StatusBatalha.EM_ANDAMENTO)).thenReturn(Optional.empty());
        when(plantelService.obterPrimeiroPokemonDisponivelParaBatalha("j-1")).thenReturn(pidgey);
        when(randomProvider.nextInt(2)).thenReturn(1);
        when(randomProvider.nextInt(3)).thenReturn(0);
        when(pokemonEspecieRepository.findByNomeIgnoreCase("rattata")).thenReturn(Optional.of(rattata));
        when(batalhaRepository.save(any(Batalha.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var dto = handler.handle(new IniciarBatalhaSelvagemCommand("j-1"));

        assertEquals("pk-slot-1", dto.pokemonJogadorId());

        ArgumentCaptor<Batalha> batalhaCaptor = ArgumentCaptor.forClass(Batalha.class);
        verify(batalhaRepository).save(batalhaCaptor.capture());
        assertEquals("pk-slot-1", batalhaCaptor.getValue().getPokemonJogadorId());
        verify(batalhaOponentePokemonRepository).save(any(BatalhaOponentePokemon.class));
    }

    private PokemonCapturado criarPokemon(String id,
                                          String jogadorId,
                                          Long especieId,
                                          String nome,
                                          TipoPokemon tipoPrimario,
                                          TipoPokemon tipoSecundario) {
        PokemonEspecie especie = new PokemonEspecie(especieId, nome, tipoPrimario, tipoSecundario, 40, 45, 40, 56, 3, 18, null, 255, "generation-i");
        return PokemonCapturado.criarInicial(id, jogadorId, especie);
    }
}
