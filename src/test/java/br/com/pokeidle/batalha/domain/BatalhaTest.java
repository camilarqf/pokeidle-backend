package br.com.pokeidle.batalha.domain;

import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.shared.domain.TipoPokemon;
import br.com.pokeidle.treinadores.domain.TreinadorNpc;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BatalhaTest {

    @Test
    void deveIniciarBatalhaContraTreinadorComIndiceDoPrimeiroOponente() throws Exception {
        PokemonEspecie bulbasaur = new PokemonEspecie(1L, "bulbasaur", TipoPokemon.GRASS, TipoPokemon.POISON, 45, 49, 49, 45, 7, 69, null, 45, "generation-i");
        PokemonEspecie geodude = new PokemonEspecie(74L, "geodude", TipoPokemon.ROCK, TipoPokemon.GROUND, 40, 80, 100, 20, 4, 200, null, 255, "generation-i");
        PokemonCapturado pokemonJogador = PokemonCapturado.criarInicial("pk-1", "j-1", bulbasaur);
        TreinadorNpc treinador = criarTreinador("Brock", 150, 250, true);
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

        assertEquals(1, batalha.getIndiceOponenteAtual());
        assertEquals("geodude", batalha.getNomeSelvagem());
    }

    private TreinadorNpc criarTreinador(String nome, int recompensaMoedas, int experienciaRecompensa, boolean lider) throws Exception {
        Constructor<TreinadorNpc> constructor = TreinadorNpc.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        TreinadorNpc treinador = constructor.newInstance();
        definirCampo(treinador, "id", 1L);
        definirCampo(treinador, "nome", nome);
        definirCampo(treinador, "noJornadaId", 6L);
        definirCampo(treinador, "ginasioId", 1L);
        definirCampo(treinador, "lider", lider);
        definirCampo(treinador, "recompensaMoedas", recompensaMoedas);
        definirCampo(treinador, "experienciaRecompensa", experienciaRecompensa);
        definirCampo(treinador, "ordemDesafio", 1);
        return treinador;
    }

    private void definirCampo(Object alvo, String nomeCampo, Object valor) throws Exception {
        Field field = alvo.getClass().getDeclaredField(nomeCampo);
        field.setAccessible(true);
        field.set(alvo, valor);
    }
}
