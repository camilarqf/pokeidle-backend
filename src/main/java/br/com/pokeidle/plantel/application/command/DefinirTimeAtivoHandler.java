package br.com.pokeidle.plantel.application.command;

import br.com.pokeidle.plantel.application.PlantelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefinirTimeAtivoHandler {

    private final PlantelService plantelService;

    public DefinirTimeAtivoHandler(PlantelService plantelService) {
        this.plantelService = plantelService;
    }

    @Transactional
    public void handle(DefinirTimeAtivoCommand command) {
        plantelService.definirTimeAtivo(command.jogadorId(), command.pokemonIdsOrdenados());
    }
}
