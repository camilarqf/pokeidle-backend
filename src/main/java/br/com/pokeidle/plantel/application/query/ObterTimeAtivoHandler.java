package br.com.pokeidle.plantel.application.query;

import br.com.pokeidle.plantel.application.PlantelService;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObterTimeAtivoHandler {

    private final PlantelService plantelService;

    public ObterTimeAtivoHandler(PlantelService plantelService) {
        this.plantelService = plantelService;
    }

    public List<PokemonDoTimeDto> handle(ObterTimeAtivoQuery query) {
        List<PokemonCapturado> time = plantelService.obterTimeAtivo(query.jogadorId());
        return java.util.stream.IntStream.range(0, time.size())
                .mapToObj(index -> toDto(time.get(index), index + 1))
                .toList();
    }

    private PokemonDoTimeDto toDto(PokemonCapturado pokemon, int slot) {
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
                true,
                slot
        );
    }
}
