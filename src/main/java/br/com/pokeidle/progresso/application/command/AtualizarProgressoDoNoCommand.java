package br.com.pokeidle.progresso.application.command;

public record AtualizarProgressoDoNoCommand(String jogadorId, Long noId, int vitoriasObtidas) {
}
