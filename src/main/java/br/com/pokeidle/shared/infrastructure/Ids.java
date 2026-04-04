package br.com.pokeidle.shared.infrastructure;

import java.util.UUID;

public final class Ids {

    private Ids() {
    }

    public static String unique() {
        return UUID.randomUUID().toString();
    }
}
