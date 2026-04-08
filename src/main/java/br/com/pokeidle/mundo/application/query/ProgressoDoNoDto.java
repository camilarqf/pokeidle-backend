package br.com.pokeidle.mundo.application.query;

import java.util.List;

public record ProgressoDoNoDto(Long noId,
                               boolean desbloqueado,
                               boolean concluido,
                               int batalhasSelvagensVencidas,
                               int treinadoresDerrotados,
                               boolean liderDerrotado,
                               List<ObjetivoProgressoDto> objetivos) {

    public record ObjetivoProgressoDto(String tipo, int atual, int alvo, boolean concluido, String descricao) {
    }
}
