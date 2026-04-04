package br.com.pokeidle.batalha.domain;

import br.com.pokeidle.shared.domain.TipoPokemon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculadoraDanoTest {

    @Test
    void deveAplicarMultiplicadorDeTipo() {
        int danoSuperEfetivo = CalculadoraDano.calcular(40, 20, TipoPokemon.GRASS, TipoPokemon.WATER);
        int danoResistido = CalculadoraDano.calcular(40, 20, TipoPokemon.GRASS, TipoPokemon.FIRE);

        assertTrue(danoSuperEfetivo > danoResistido);
    }
}
