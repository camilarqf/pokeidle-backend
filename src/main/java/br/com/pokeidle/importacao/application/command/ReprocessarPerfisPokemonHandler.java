package br.com.pokeidle.importacao.application.command;

import br.com.pokeidle.catalogo.domain.PokemonEspecieRepository;
import br.com.pokeidle.catalogo.domain.PokemonPerfilDesign;
import br.com.pokeidle.catalogo.domain.PokemonPerfilDesignRepository;
import br.com.pokeidle.shared.domain.TipoPokemon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReprocessarPerfisPokemonHandler {

    private final PokemonEspecieRepository pokemonEspecieRepository;
    private final PokemonPerfilDesignRepository pokemonPerfilDesignRepository;

    public ReprocessarPerfisPokemonHandler(PokemonEspecieRepository pokemonEspecieRepository,
                                           PokemonPerfilDesignRepository pokemonPerfilDesignRepository) {
        this.pokemonEspecieRepository = pokemonEspecieRepository;
        this.pokemonPerfilDesignRepository = pokemonPerfilDesignRepository;
    }

    @Transactional
    public List<Long> handle(ReprocessarPerfisPokemonCommand command) {
        return pokemonEspecieRepository.findAll().stream()
                .map(especie -> {
                    PokemonPerfilDesign design = pokemonPerfilDesignRepository.findByPokemonEspecieId(especie.getId())
                            .orElseGet(() -> new PokemonPerfilDesign(especie.getId(), especie.getNome(), corPorTipo(especie.getTipoPrimario())));
                    design.atualizar(especie.getNome(), corPorTipo(especie.getTipoPrimario()));
                    pokemonPerfilDesignRepository.save(design);
                    return especie.getId();
                })
                .toList();
    }

    private String corPorTipo(TipoPokemon tipoPokemon) {
        return switch (tipoPokemon) {
            case GRASS -> "#78C850";
            case FIRE -> "#F08030";
            case WATER -> "#6890F0";
            case FLYING -> "#A890F0";
            case POISON -> "#A040A0";
            case ELECTRIC -> "#F8D030";
            case BUG -> "#A8B820";
            case ROCK -> "#B8A038";
            case GROUND -> "#E0C068";
            default -> "#A8A878";
        };
    }
}
