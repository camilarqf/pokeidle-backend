package br.com.pokeidle.captura.application.command;

public record ResultadoCapturaDto(boolean capturado,
                                  double chanceCalculada,
                                  String pokemonCapturadoId,
                                  String statusBatalha) {
}
