package br.com.pokeidle.treinadores.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "jogador_treinador_derrotado")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JogadorTreinadorDerrotado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jogador_id", nullable = false, length = 36)
    private String jogadorId;

    @Column(name = "treinador_npc_id", nullable = false)
    private Long treinadorNpcId;

    @Column(name = "derrotado_em", nullable = false)
    private LocalDateTime derrotadoEm;

    public JogadorTreinadorDerrotado(String jogadorId, Long treinadorNpcId) {
        this.jogadorId = jogadorId;
        this.treinadorNpcId = treinadorNpcId;
        this.derrotadoEm = LocalDateTime.now();
    }
}
