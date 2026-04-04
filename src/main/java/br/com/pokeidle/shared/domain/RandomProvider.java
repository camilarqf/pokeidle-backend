package br.com.pokeidle.shared.domain;

public interface RandomProvider {

    int nextInt(int bound);

    double nextDouble();
}
