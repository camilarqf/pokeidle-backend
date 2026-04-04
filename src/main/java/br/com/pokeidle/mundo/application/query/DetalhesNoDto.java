package br.com.pokeidle.mundo.application.query;

import br.com.pokeidle.mundo.domain.TipoNo;

public record DetalhesNoDto(Long id,
                            String nome,
                            String descricao,
                            TipoNo tipo,
                            boolean permiteBatalhaSelvagem,
                            boolean cidadeHub,
                            MissaoDto missao) {

    public record MissaoDto(String tipo, int alvoQuantidade, String descricao) {
    }
}
