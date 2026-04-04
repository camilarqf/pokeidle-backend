package br.com.pokeidle.inventario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "inventario_jogador")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InventarioJogador {

    @Id
    private String id;

    @Column(name = "jogador_id", nullable = false, length = 36)
    private String jogadorId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private int quantidade;

    public InventarioJogador(String id, String jogadorId, Long itemId, int quantidade) {
        this.id = id;
        this.jogadorId = jogadorId;
        this.itemId = itemId;
        this.quantidade = quantidade;
    }

    public void adicionar(int quantidade) {
        this.quantidade += quantidade;
    }

    public void remover(int quantidade) {
        if (this.quantidade < quantidade) {
            throw new IllegalStateException("Quantidade insuficiente no inventario.");
        }
        this.quantidade -= quantidade;
    }
}
