package br.com.pokeidle.progresso.domain;

import br.com.pokeidle.mundo.domain.ObjetivoMissaoNo;
import br.com.pokeidle.mundo.domain.TipoObjetivoMissaoNo;
import br.com.pokeidle.shared.domain.AggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "batalhas_selvagens_vencidas", nullable = false)
    private int batalhasSelvagensVencidas;

    @Column(name = "treinadores_derrotados", nullable = false)
    private int treinadoresDerrotados;

    @Column(name = "lider_derrotado", nullable = false)
    private boolean liderDerrotado;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    public ProgressoNoJogador(String id, String jogadorId, Long noJornadaId, boolean desbloqueado) {
        this.id = id;
        this.jogadorId = jogadorId;
        this.noJornadaId = noJornadaId;
        this.desbloqueado = desbloqueado;
        this.concluido = false;
        this.batalhasVencidas = 0;
        this.batalhasSelvagensVencidas = 0;
        this.treinadoresDerrotados = 0;
        this.liderDerrotado = false;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void registrarVitorias(int quantidade, int alvoConclusao) {
        this.batalhasVencidas += quantidade;
        if (this.batalhasVencidas >= alvoConclusao) {
            this.concluido = true;
        }
        this.atualizadoEm = LocalDateTime.now();
    }

    public void registrarEntradaNo(List<ObjetivoMissaoNo> objetivos) {
        this.batalhasVencidas = Math.max(this.batalhasVencidas, 1);
        atualizarConclusao(objetivos);
    }

    public void registrarVitoriaSelvagem(int quantidade, List<ObjetivoMissaoNo> objetivos) {
        this.batalhasVencidas += quantidade;
        this.batalhasSelvagensVencidas += quantidade;
        atualizarConclusao(objetivos);
    }

    public void registrarTreinadorDerrotado(List<ObjetivoMissaoNo> objetivos) {
        this.treinadoresDerrotados += 1;
        atualizarConclusao(objetivos);
    }

    public void registrarLiderDerrotado(List<ObjetivoMissaoNo> objetivos) {
        this.liderDerrotado = true;
        atualizarConclusao(objetivos);
    }

    public boolean desbloquear() {
        if (desbloqueado) {
            return false;
        }
        this.desbloqueado = true;
        this.atualizadoEm = LocalDateTime.now();
        return true;
    }

    private void atualizarConclusao(List<ObjetivoMissaoNo> objetivos) {
        boolean concluiaAntes = concluido;
        concluido = objetivos.stream().allMatch(this::objetivoConcluido);
        atualizadoEm = LocalDateTime.now();
        if (!concluiaAntes && concluido) {
            registerEvent(new MissaoDeNoConcluidaDomainEvent(jogadorId, noJornadaId));
        }
    }

    private boolean objetivoConcluido(ObjetivoMissaoNo objetivo) {
        if (objetivo.getTipoObjetivo() == TipoObjetivoMissaoNo.ENTRAR_NO) {
            return batalhasVencidas >= objetivo.getAlvoQuantidade();
        }
        if (objetivo.getTipoObjetivo() == TipoObjetivoMissaoNo.VENCER_BATALHAS_SELVAGENS) {
            return batalhasSelvagensVencidas >= objetivo.getAlvoQuantidade();
        }
        if (objetivo.getTipoObjetivo() == TipoObjetivoMissaoNo.DERROTAR_TREINADORES) {
            return treinadoresDerrotados >= objetivo.getAlvoQuantidade();
        }
        if (objetivo.getTipoObjetivo() == TipoObjetivoMissaoNo.DERROTAR_LIDER) {
            return liderDerrotado;
        }
        return false;
    }
}
