package br.com.pokeidle.plantel.application.query;

import br.com.pokeidle.plantel.application.PlantelService;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObterBoxPokemonHandler {

    private final PlantelService plantelService;

    public ObterBoxPokemonHandler(PlantelService plantelService) {
        this.plantelService = plantelService;
    }

    public List<PokemonDoTimeDto> handle(ObterBoxPokemonQuery query) {
        return plantelService.obterBox(query.jogadorId()).stream()
                .map(this::toDto)
                .toList();
    }

    private PokemonDoTimeDto toDto(PokemonCapturado pokemon) {
        return new PokemonDoTimeDto(
                pokemon.getId(),
                pokemon.getEspecieId(),
                pokemon.getNome(),
                pokemon.getNivel(),
                pokemon.getExperiencia(),
                pokemon.getExperienciaParaProximoNivel(),
                pokemon.getHpAtual(),
                pokemon.getHpMaximo(),
                pokemon.getAtaque(),
                pokemon.getDefesa(),
                pokemon.getVelocidade(),
                pokemon.isInicial(),
                false,
                null
        );
    }
}
