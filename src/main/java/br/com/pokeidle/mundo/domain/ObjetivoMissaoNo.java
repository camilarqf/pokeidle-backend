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
@Table(name = "objetivo_missao_no")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ObjetivoMissaoNo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "missao_no_id", nullable = false)
    private Long missaoNoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_objetivo", nullable = false, length = 40)
    private TipoObjetivoMissaoNo tipoObjetivo;

    @Column(name = "alvo_quantidade", nullable = false)
    private int alvoQuantidade;

    @Column(nullable = false, length = 160)
    private String descricao;

    @Column(nullable = false)
    private int ordem;

    public ObjetivoMissaoNo(Long missaoNoId,
                            TipoObjetivoMissaoNo tipoObjetivo,
                            int alvoQuantidade,
                            String descricao,
                            int ordem) {
        this.missaoNoId = missaoNoId;
        this.tipoObjetivo = tipoObjetivo;
        this.alvoQuantidade = alvoQuantidade;
        this.descricao = descricao;
        this.ordem = ordem;
    }
}
