package br.com.pokeidle.mundo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "no_jornada")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoJornada {

    @Id
    private Long id;

    @Column(name = "regiao_id", nullable = false)
    private Long regiaoId;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false, unique = true, length = 80)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoNo tipo;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(name = "ordem_mapa", nullable = false)
    private int ordemMapa;

    @Column(name = "desbloqueado_inicial", nullable = false)
    private boolean desbloqueadoInicial;

    @Column(name = "permite_batalha_selvagem", nullable = false)
    private boolean permiteBatalhaSelvagem;

    @Column(name = "cidade_hub", nullable = false)
    private boolean cidadeHub;

    public NoJornada(Long id,
                     Long regiaoId,
                     String nome,
                     String slug,
                     TipoNo tipo,
                     String descricao,
                     int ordemMapa,
                     boolean desbloqueadoInicial,
                     boolean permiteBatalhaSelvagem,
                     boolean cidadeHub) {
        this.id = id;
        this.regiaoId = regiaoId;
        this.nome = nome;
        this.slug = slug;
        this.tipo = tipo;
        this.descricao = descricao;
        this.ordemMapa = ordemMapa;
        this.desbloqueadoInicial = desbloqueadoInicial;
        this.permiteBatalhaSelvagem = permiteBatalhaSelvagem;
        this.cidadeHub = cidadeHub;
    }

    public boolean ehCidade() {
        return tipo == TipoNo.CIDADE;
    }
}
