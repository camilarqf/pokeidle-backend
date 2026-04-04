package br.com.pokeidle.shared.infrastructure;

import br.com.pokeidle.shared.domain.RandomProvider;

import java.util.concurrent.ThreadLocalRandom;

public class DefaultRandomProvider implements RandomProvider {

    @Override
    public int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    @Override
    public double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
}
