package br.com.pokeidle.plantel.domain;

import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.shared.domain.TipoPokemon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PokemonCapturadoTest {

    @Test
    void deveSubirDeNivelEMelhorarStatsAoGanharExperiencia() {
        PokemonEspecie bulbasaur = new PokemonEspecie(1L, "bulbasaur", TipoPokemon.GRASS, TipoPokemon.POISON, 45, 49, 49, 45, 7, 69, null, 45, "generation-i");
        PokemonCapturado pokemon = PokemonCapturado.criarInicial("pk-1", "j-1", bulbasaur);
        int ataqueInicial = pokemon.getAtaque();
        int hpMaximoInicial = pokemon.getHpMaximo();

        pokemon.ganharExperiencia(200);

        assertTrue(pokemon.getNivel() > 5);
        assertTrue(pokemon.getAtaque() > ataqueInicial);
        assertTrue(pokemon.getHpMaximo() > hpMaximoInicial);
    }
}
