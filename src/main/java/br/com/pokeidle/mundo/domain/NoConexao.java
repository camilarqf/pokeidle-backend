package br.com.pokeidle.mundo.domain;

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
@Table(name = "no_conexao")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoConexao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origem_no_id", nullable = false)
    private Long origemNoId;

    @Column(name = "destino_no_id", nullable = false)
    private Long destinoNoId;

    public NoConexao(Long origemNoId, Long destinoNoId) {
        this.origemNoId = origemNoId;
        this.destinoNoId = destinoNoId;
    }
}
