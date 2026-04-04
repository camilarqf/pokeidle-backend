package br.com.pokeidle.captura.domain;

public final class CalculadoraCaptura {

    private CalculadoraCaptura() {
    }

    public static double chance(int hpAtual, int hpMaximo, int taxaCaptura) {
        double hpPercentual = hpMaximo == 0 ? 1.0 : (double) hpAtual / hpMaximo;
        double fatorVida = 1.0 - hpPercentual;
        double fatorEspecie = Math.min(1.0, taxaCaptura / 255.0);
        return Math.min(0.95, 0.25 + (fatorVida * 0.45) + (fatorEspecie * 0.30));
    }
}
