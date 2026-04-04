package br.com.pokeidle.importacao.application.command;

import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.catalogo.domain.PokemonEspecieRepository;
import br.com.pokeidle.catalogo.domain.PokemonPerfilDesign;
import br.com.pokeidle.catalogo.domain.PokemonPerfilDesignRepository;
import br.com.pokeidle.importacao.infrastructure.PokeApiClient;
import br.com.pokeidle.importacao.infrastructure.PokeApiPokemonResponse;
import br.com.pokeidle.importacao.infrastructure.PokeApiSpeciesResponse;
import br.com.pokeidle.shared.domain.BusinessException;
import br.com.pokeidle.shared.domain.TipoPokemon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;

@Service
public class ImportarPokemonPorIdOuNomeHandler {

    private final PokeApiClient pokeApiClient;
    private final PokemonEspecieRepository pokemonEspecieRepository;
    private final PokemonPerfilDesignRepository pokemonPerfilDesignRepository;

    public ImportarPokemonPorIdOuNomeHandler(PokeApiClient pokeApiClient,
                                             PokemonEspecieRepository pokemonEspecieRepository,
                                             PokemonPerfilDesignRepository pokemonPerfilDesignRepository) {
        this.pokeApiClient = pokeApiClient;
        this.pokemonEspecieRepository = pokemonEspecieRepository;
        this.pokemonPerfilDesignRepository = pokemonPerfilDesignRepository;
    }

    @Transactional
    public PokemonImportadoDto handle(ImportarPokemonPorIdOuNomeCommand command) {
        PokeApiPokemonResponse pokemon = pokeApiClient.buscarPokemon(command.nomeOuId().toLowerCase());
        PokeApiSpeciesResponse species = pokeApiClient.buscarSpecies(command.nomeOuId().toLowerCase());

        TipoPokemon tipoPrimario = pokemon.types().stream()
                .sorted(Comparator.comparingInt(PokeApiPokemonResponse.PokeApiTypeSlot::slot))
                .map(slot -> mapearTipo(slot.type().name()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Pokemon sem tipo principal."));
        TipoPokemon tipoSecundario = pokemon.types().stream()
                .sorted(Comparator.comparingInt(PokeApiPokemonResponse.PokeApiTypeSlot::slot))
                .skip(1)
                .map(slot -> mapearTipo(slot.type().name()))
                .findFirst()
                .orElse(null);

        PokemonEspecie entidade = pokemonEspecieRepository.findById(pokemon.id())
                .orElseGet(() -> new PokemonEspecie(
                        pokemon.id(),
                        pokemon.name(),
                        tipoPrimario,
                        tipoSecundario,
                        stat(pokemon, "hp"),
                        stat(pokemon, "attack"),
                        stat(pokemon, "defense"),
                        stat(pokemon, "speed"),
                        pokemon.height(),
                        pokemon.weight(),
                        pokemon.sprites() == null ? null : pokemon.sprites().front_default(),
                        species.capture_rate(),
                        species.generation().name()
                ));
        entidade.atualizarImportacao(
                pokemon.name(),
                tipoPrimario,
                tipoSecundario,
                stat(pokemon, "hp"),
                stat(pokemon, "attack"),
                stat(pokemon, "defense"),
                stat(pokemon, "speed"),
                pokemon.height(),
                pokemon.weight(),
                pokemon.sprites() == null ? null : pokemon.sprites().front_default(),
                species.capture_rate(),
                species.generation().name()
        );
        pokemonEspecieRepository.save(entidade);

        PokemonPerfilDesign design = pokemonPerfilDesignRepository.findByPokemonEspecieId(entidade.getId())
                .orElseGet(() -> new PokemonPerfilDesign(entidade.getId(), entidade.getNome(), corPorTipo(entidade.getTipoPrimario())));
        design.atualizar(entidade.getNome(), corPorTipo(entidade.getTipoPrimario()));
        pokemonPerfilDesignRepository.save(design);

        return new PokemonImportadoDto(
                entidade.getId(),
                entidade.getNome(),
                entidade.getTipoPrimario(),
                entidade.getTipoSecundario(),
                entidade.getHpBase(),
                entidade.getAtaqueBase(),
                entidade.getDefesaBase(),
                entidade.getVelocidadeBase(),
                entidade.getAltura(),
                entidade.getPeso(),
                entidade.getSpritePrincipal(),
                entidade.getTaxaCaptura(),
                entidade.getGeracao()
        );
    }

    private static int stat(PokeApiPokemonResponse pokemon, String nomeStat) {
        return pokemon.stats().stream()
                .filter(stat -> stat.stat().name().equalsIgnoreCase(nomeStat))
                .findFirst()
                .map(PokeApiPokemonResponse.PokeApiStatSlot::base_stat)
                .orElseThrow(() -> new BusinessException("Stat nao encontrada: " + nomeStat));
    }

    private static TipoPokemon mapearTipo(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "grass" -> TipoPokemon.GRASS;
            case "fire" -> TipoPokemon.FIRE;
            case "water" -> TipoPokemon.WATER;
            case "flying" -> TipoPokemon.FLYING;
            case "poison" -> TipoPokemon.POISON;
            case "normal" -> TipoPokemon.NORMAL;
            default -> throw new BusinessException("Tipo nao suportado no v0: " + tipo);
        };
    }

    private static String corPorTipo(TipoPokemon tipoPokemon) {
        return switch (tipoPokemon) {
            case GRASS -> "#78C850";
            case FIRE -> "#F08030";
            case WATER -> "#6890F0";
            case FLYING -> "#A890F0";
            case POISON -> "#A040A0";
            default -> "#A8A878";
        };
    }
}
