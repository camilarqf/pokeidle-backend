package br.com.pokeidle.plantel.application.command;

import br.com.pokeidle.plantel.application.PlantelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MoverPokemonEntreTimeEBoxHandler {

    private final PlantelService plantelService;

    public MoverPokemonEntreTimeEBoxHandler(PlantelService plantelService) {
        this.plantelService = plantelService;
    }

    @Transactional
    public void handle(MoverPokemonEntreTimeEBoxCommand command) {
        plantelService.moverEntreTimeEBox(command.jogadorId(), command.pokemonId(), command.paraBox(), command.slotDestino());
    }
}
