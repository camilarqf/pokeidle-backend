package br.com.pokeidle.mundo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "regiao")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Regiao {

    @Id
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    public Regiao(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
