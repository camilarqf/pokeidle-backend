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
@Table(name = "treinador_npc")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TreinadorNpc {

    @Id
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(name = "no_jornada_id", nullable = false)
    private Long noJornadaId;

    @Column(name = "ginasio_id")
    private Long ginasioId;

    @Column(nullable = false)
    private boolean lider;

    @Column(name = "recompensa_moedas", nullable = false)
    private int recompensaMoedas;

    @Column(name = "experiencia_recompensa", nullable = false)
    private int experienciaRecompensa;

    @Column(name = "ordem_desafio", nullable = false)
    private int ordemDesafio;

    public boolean pertenceAoGinasio(Long ginasioId) {
        return this.ginasioId != null && this.ginasioId.equals(ginasioId);
    }
}
