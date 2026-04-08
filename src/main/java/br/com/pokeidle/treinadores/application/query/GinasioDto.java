package br.com.pokeidle.treinadores.application.query;

public record GinasioDto(Long id,
                         String nome,
                         Long cidadeId,
                         Long noJornadaId,
                         String badgeCodigo,
                         String badgeNome,
                         Long liderTreinadorId) {
}
