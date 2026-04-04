package br.com.pokeidle.batalha.domain;

import br.com.pokeidle.shared.domain.TipoPokemon;

public final class CalculadoraDano {

    private CalculadoraDano() {
    }

    public static int calcular(int ataque, int defesa, TipoPokemon tipoAtaque, TipoPokemon tipoAlvo) {
        double base = Math.max(1, ataque - (defesa * 0.45));
        double multiplicador = multiplicador(tipoAtaque, tipoAlvo);
        return Math.max(1, (int) Math.round(base * multiplicador));
    }

    static double multiplicador(TipoPokemon atacante, TipoPokemon defensor) {
        if (atacante == null || defensor == null) {
            return 1.0;
        }
        return switch (atacante) {
            case FIRE -> defensor == TipoPokemon.GRASS ? 1.5 : defensor == TipoPokemon.WATER ? 0.75 : 1.0;
            case WATER -> defensor == TipoPokemon.FIRE ? 1.5 : defensor == TipoPokemon.GRASS ? 0.75 : 1.0;
            case GRASS -> defensor == TipoPokemon.WATER ? 1.5 : defensor == TipoPokemon.FIRE ? 0.75 : 1.0;
            default -> 1.0;
        };
    }
}
