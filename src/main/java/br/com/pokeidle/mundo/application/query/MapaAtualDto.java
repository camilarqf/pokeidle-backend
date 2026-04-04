package br.com.pokeidle.mundo.application.query;

import br.com.pokeidle.mundo.domain.TipoNo;

import java.util.List;

public record MapaAtualDto(Long noAtualId, List<NoMapaDto> nos) {

    public record NoMapaDto(Long id,
                            String nome,
                            TipoNo tipo,
                            boolean desbloqueado,
                            boolean concluido,
                            boolean atual) {
    }
}
