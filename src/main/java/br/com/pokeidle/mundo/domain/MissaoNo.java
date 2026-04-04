package br.com.pokeidle.mundo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "missao_no")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissaoNo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "no_jornada_id", nullable = false, unique = true)
    private Long noJornadaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_missao", nullable = false, length = 30)
    private TipoMissaoNo tipoMissao;

    @Column(name = "alvo_quantidade", nullable = false)
    private int alvoQuantidade;

    @Column(nullable = false, length = 120)
    private String descricao;

    public MissaoNo(Long noJornadaId, TipoMissaoNo tipoMissao, int alvoQuantidade, String descricao) {
        this.noJornadaId = noJornadaId;
        this.tipoMissao = tipoMissao;
        this.alvoQuantidade = alvoQuantidade;
        this.descricao = descricao;
    }
}
