package br.com.pokeidle.importacao.infrastructure;

public record PokeApiSpeciesResponse(Long id,
                                     String name,
                                     int capture_rate,
                                     PokeApiGeneration generation) {

    public record PokeApiGeneration(String name, String url) {
    }
}
