package br.com.pokeidle.captura.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculadoraCapturaTest {

    @Test
    void deveAumentarChanceQuandoPokemonEstaComPoucoHp() {
        double chanceComVidaAlta = CalculadoraCaptura.chance(30, 40, 190);
        double chanceComVidaBaixa = CalculadoraCaptura.chance(5, 40, 190);

        assertTrue(chanceComVidaBaixa > chanceComVidaAlta);
    }
}
