package br.com.pokeidle.progresso.domain;

import br.com.pokeidle.shared.domain.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "progresso_no_jogador")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgressoNoJogador extends AggregateRoot {

    @Id
    private String id;

    @Column(name = "jogador_id", nullable = false, length = 36)
    private String jogadorId;

    @Column(name = "no_jornada_id", nullable = false)
    private Long noJornadaId;

    @Column(nullable = false)
    private boolean desbloqueado;

    @Column(nullable = false)
    private boolean concluido;

    @Column(name = "batalhas_vencidas", nullable = false)
    private int batalhasVencidas;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    public ProgressoNoJogador(String id, String jogadorId, Long noJornadaId, boolean desbloqueado) {
        this.id = id;
        this.jogadorId = jogadorId;
        this.noJornadaId = noJornadaId;
        this.desbloqueado = desbloqueado;
        this.concluido = false;
        this.batalhasVencidas = 0;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void registrarVitorias(int quantidade, int alvoConclusao) {
        this.batalhasVencidas += quantidade;
        if (this.batalhasVencidas >= alvoConclusao) {
            this.concluido = true;
        }
        this.atualizadoEm = LocalDateTime.now();
    }

    public boolean desbloquear() {
        if (desbloqueado) {
            return false;
        }
        this.desbloqueado = true;
        this.atualizadoEm = LocalDateTime.now();
        return true;
    }
}
