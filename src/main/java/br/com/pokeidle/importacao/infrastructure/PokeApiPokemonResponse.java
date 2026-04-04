package br.com.pokeidle.importacao.infrastructure;

import java.util.List;

public record PokeApiPokemonResponse(Long id,
                                     String name,
                                     int height,
                                     int weight,
                                     List<PokeApiTypeSlot> types,
                                     List<PokeApiStatSlot> stats,
                                     PokeApiSprites sprites) {

    public record PokeApiTypeSlot(int slot, PokeApiNamedResource type) {
    }

    public record PokeApiStatSlot(int base_stat, PokeApiNamedResource stat) {
    }

    public record PokeApiSprites(String front_default) {
    }

    public record PokeApiNamedResource(String name, String url) {
    }
}
