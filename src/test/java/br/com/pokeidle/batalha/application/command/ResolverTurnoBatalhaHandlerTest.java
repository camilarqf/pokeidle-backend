package br.com.pokeidle.batalha.application.command;

import br.com.pokeidle.batalha.domain.Batalha;
import br.com.pokeidle.batalha.domain.BatalhaOponentePokemon;
import br.com.pokeidle.batalha.domain.BatalhaOponentePokemonRepository;
import br.com.pokeidle.batalha.domain.BatalhaRepository;
import br.com.pokeidle.batalha.domain.StatusBatalha;
import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.plantel.application.PlantelService;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
import br.com.pokeidle.shared.application.DomainEventPublisher;
import br.com.pokeidle.shared.domain.TipoPokemon;
import br.com.pokeidle.treinadores.domain.TreinadorNpc;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ResolverTurnoBatalhaHandlerTest {

    @Test
    void deveRecuperarBatalhaDeTreinadorComIndiceDeOponenteInconsistente() throws Exception {
        BatalhaRepository batalhaRepository = mock(BatalhaRepository.class);
        BatalhaOponentePokemonRepository batalhaOponentePokemonRepository = mock(BatalhaOponentePokemonRepository.class);
        PokemonCapturadoRepository pokemonCapturadoRepository = mock(PokemonCapturadoRepository.class);
        JogadorRepository jogadorRepository = mock(JogadorRepository.class);
        PlantelService plantelService = mock(PlantelService.class);
        DomainEventPublisher domainEventPublisher = mock(DomainEventPublisher.class);
        ResolverTurnoBatalhaHandler handler = new ResolverTurnoBatalhaHandler(
                batalhaRepository,
                batalhaOponentePokemonRepository,
                pokemonCapturadoRepository,
                jogadorRepository,
                plantelService,
                domainEventPublisher
        );

        PokemonEspecie bulbasaur = new PokemonEspecie(1L, "bulbasaur", TipoPokemon.GRASS, TipoPokemon.POISON, 45, 49, 49, 45, 7, 69, null, 45, "generation-i");
        PokemonEspecie geodude = new PokemonEspecie(74L, "geodude", TipoPokemon.ROCK, TipoPokemon.GROUND, 40, 80, 100, 20, 4, 200, null, 255, "generation-i");
        PokemonCapturado pokemonJogador = PokemonCapturado.criarInicial("pk-1", "j-1", bulbasaur);
        Jogador jogador = Jogador.criar("j-1", "Red", 100L);
        TreinadorNpc treinador = criarTreinador();
        BatalhaOponentePokemon primeiroOponente = new BatalhaOponentePokemon(
                "b-1",
                1,
                geodude.getId(),
                geodude.getNome(),
                12,
                40,
                40,
                30,
                35,
                15,
                geodude.getTipoPrimario(),
                geodude.getTipoSecundario()
        );
        Batalha batalha = Batalha.criarContraTreinador("b-1", "j-1", 6L, pokemonJogador, treinador, primeiroOponente);
        definirCampo(batalha, "indiceOponenteAtual", 0);

        when(batalhaRepository.findById("b-1")).thenReturn(Optional.of(batalha));
        when(jogadorRepository.findById("j-1")).thenReturn(Optional.of(jogador));
        when(pokemonCapturadoRepository.findById("pk-1")).thenReturn(Optional.of(pokemonJogador));
        when(batalhaOponentePokemonRepository.findByBatalhaIdAndOrdemEquipe("b-1", 0)).thenReturn(Optional.empty());
        when(batalhaOponentePokemonRepository.findByBatalhaIdOrderByOrdemEquipeAsc("b-1")).thenReturn(List.of(primeiroOponente));

        var dto = handler.handle(new ResolverTurnoBatalhaCommand("b-1"));

        assertEquals(1, batalha.getIndiceOponenteAtual());
        assertEquals(1, dto.turnos());
        assertNotEquals(StatusBatalha.DERROTA.name(), dto.status());
        verify(batalhaRepository).save(batalha);
        verify(batalhaOponentePokemonRepository).save(primeiroOponente);
        verify(pokemonCapturadoRepository).save(pokemonJogador);
        verify(domainEventPublisher).publishAll(any());
    }

    private TreinadorNpc criarTreinador() throws Exception {
        Constructor<TreinadorNpc> constructor = TreinadorNpc.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        TreinadorNpc treinador = constructor.newInstance();
        definirCampo(treinador, "id", 205L);
        definirCampo(treinador, "nome", "Brock");
        definirCampo(treinador, "noJornadaId", 6L);
        definirCampo(treinador, "ginasioId", 301L);
        definirCampo(treinador, "lider", true);
        definirCampo(treinador, "recompensaMoedas", 500);
        definirCampo(treinador, "experienciaRecompensa", 260);
        definirCampo(treinador, "ordemDesafio", 2);
        return treinador;
    }

    private static void definirCampo(Object alvo, String nomeCampo, Object valor) throws Exception {
        Field field = alvo.getClass().getDeclaredField(nomeCampo);
        field.setAccessible(true);
        field.set(alvo, valor);
    }
}
