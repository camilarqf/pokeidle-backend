package br.com.pokeidle.inventario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private CodigoItem codigo;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(name = "preco_base", nullable = false)
    private int precoBase;

    public Item(Long id, CodigoItem codigo, String nome, int precoBase) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.precoBase = precoBase;
    }
}
