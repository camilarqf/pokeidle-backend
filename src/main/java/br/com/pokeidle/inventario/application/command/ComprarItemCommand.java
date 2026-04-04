package br.com.pokeidle.inventario.application.command;

public record ComprarItemCommand(String jogadorId, Long cidadeId, Long itemId, int quantidade) {
}
