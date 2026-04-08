package br.com.pokeidle.jogador.domain;

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

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "badge_jogador")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BadgeJogador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jogador_id", nullable = false, length = 36)
    private String jogadorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private CodigoBadge codigo;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(name = "obtida_em", nullable = false)
    private LocalDateTime obtidaEm;

    public BadgeJogador(String jogadorId, CodigoBadge codigo, String nome) {
        this.jogadorId = jogadorId;
        this.codigo = codigo;
        this.nome = nome;
        this.obtidaEm = LocalDateTime.now();
    }
}
