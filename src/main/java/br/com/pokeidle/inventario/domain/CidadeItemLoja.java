package br.com.pokeidle.inventario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "cidade_item_loja")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CidadeItemLoja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cidade_id", nullable = false)
    private Long cidadeId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private int preco;

    public CidadeItemLoja(Long cidadeId, Long itemId, int preco) {
        this.cidadeId = cidadeId;
        this.itemId = itemId;
        this.preco = preco;
    }
}
