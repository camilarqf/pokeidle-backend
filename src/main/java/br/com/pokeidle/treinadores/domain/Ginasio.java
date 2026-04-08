package br.com.pokeidle.treinadores.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "ginasio")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ginasio {

    @Id
    private Long id;

    @Column(name = "no_jornada_id", nullable = false)
    private Long noJornadaId;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(name = "cidade_id", nullable = false)
    private Long cidadeId;

    @Column(name = "lider_treinador_id")
    private Long liderTreinadorId;

    @Column(name = "badge_codigo", nullable = false, length = 40)
    private String badgeCodigo;

    @Column(name = "badge_nome", nullable = false, length = 80)
    private String badgeNome;
}
