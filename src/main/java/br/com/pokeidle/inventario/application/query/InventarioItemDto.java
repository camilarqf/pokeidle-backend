package br.com.pokeidle.inventario.application.query;

public record InventarioItemDto(Long itemId, String codigo, String nome, int quantidade) {
}
