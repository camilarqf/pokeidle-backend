package br.com.pokeidle.treinadores.application.query;

import java.util.List;

public record StatusDoGinasioDto(Long ginasioId,
                                 String nome,
                                 boolean badgeObtida,
                                 boolean liderDisponivel,
                                 List<TreinadorStatusDto> treinadores) {

    public record TreinadorStatusDto(Long treinadorId, String nome, boolean lider, boolean derrotado, int ordemDesafio) {
    }
}
