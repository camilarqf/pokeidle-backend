package br.com.pokeidle.batalha.application.query;

import br.com.pokeidle.batalha.domain.Batalha;

public final class BatalhaMapper {

    private BatalhaMapper() {
    }

    public static BatalhaDto toDto(Batalha batalha) {
        return new BatalhaDto(
                batalha.getId(),
                batalha.getJogadorId(),
                batalha.getNoJornadaId(),
                batalha.getTipo().name(),
                batalha.getPokemonJogadorId(),
                batalha.getNomeOponente(),
                batalha.getTreinadorNpcId(),
                batalha.getEspecieSelvagemId(),
                batalha.getNomeSelvagem(),
                batalha.getNivelSelvagem(),
                batalha.getHpAtualSelvagem(),
                batalha.getHpMaximoSelvagem(),
                batalha.getStatus().name(),
                batalha.getTurnos(),
                batalha.getExperienciaConcedida(),
                batalha.getRecompensaMoedas(),
                batalha.isCapturaPermitida()
        );
    }
}
