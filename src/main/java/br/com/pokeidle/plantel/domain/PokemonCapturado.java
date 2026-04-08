package br.com.pokeidle.plantel.domain;

import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.shared.domain.AggregateRoot;
import br.com.pokeidle.shared.domain.TipoPokemon;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "pokemon_capturado")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PokemonCapturado extends AggregateRoot {

    @Id
    private String id;

    @Column(name = "jogador_id", nullable = false, length = 36)
    private String jogadorId;

    @Column(name = "especie_id", nullable = false)
    private Long especieId;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false)
    private int nivel;

    @Column(nullable = false)
    private int experiencia;

    @Column(name = "hp_atual", nullable = false)
    private int hpAtual;

    @Column(name = "hp_maximo", nullable = false)
    private int hpMaximo;

    @Column(nullable = false)
    private int ataque;

    @Column(nullable = false)
    private int defesa;

    @Column(nullable = false)
    private int velocidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_primario", nullable = false, length = 20)
    private TipoPokemon tipoPrimario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_secundario", length = 20)
    private TipoPokemon tipoSecundario;

    @Column(nullable = false)
    private boolean inicial;

    @Column(nullable = false)
    private boolean ativo;

    @Column(name = "capturado_em", nullable = false)
    private LocalDateTime capturadoEm;

    private PokemonCapturado(String id,
                             String jogadorId,
                             Long especieId,
                             String nome,
                             int nivel,
                             int experiencia,
                             int hpAtual,
                             int hpMaximo,
                             int ataque,
                             int defesa,
                             int velocidade,
                             TipoPokemon tipoPrimario,
                             TipoPokemon tipoSecundario,
                             boolean inicial,
                             boolean ativo,
                             LocalDateTime capturadoEm) {
        this.id = id;
        this.jogadorId = jogadorId;
        this.especieId = especieId;
        this.nome = nome;
        this.nivel = nivel;
        this.experiencia = experiencia;
        this.hpAtual = hpAtual;
        this.hpMaximo = hpMaximo;
        this.ataque = ataque;
        this.defesa = defesa;
        this.velocidade = velocidade;
        this.tipoPrimario = tipoPrimario;
        this.tipoSecundario = tipoSecundario;
        this.inicial = inicial;
        this.ativo = ativo;
        this.capturadoEm = capturadoEm;
    }

    public static PokemonCapturado criarInicial(String id, String jogadorId, PokemonEspecie especie) {
        return criar(id, jogadorId, especie, 5, true, true);
    }

    public static PokemonCapturado criarSelvagemCapturado(String id, String jogadorId, PokemonEspecie especie, int nivel) {
        return criar(id, jogadorId, especie, nivel, false, false);
    }

    private static PokemonCapturado criar(String id,
                                          String jogadorId,
                                          PokemonEspecie especie,
                                          int nivel,
                                          boolean inicial,
                                          boolean ativo) {
        int hp = calcularStat(especie.getHpBase(), nivel, 10);
        return new PokemonCapturado(
                id,
                jogadorId,
                especie.getId(),
                especie.getNome(),
                nivel,
                0,
                hp,
                hp,
                calcularStat(especie.getAtaqueBase(), nivel, 5),
                calcularStat(especie.getDefesaBase(), nivel, 5),
                calcularStat(especie.getVelocidadeBase(), nivel, 5),
                especie.getTipoPrimario(),
                especie.getTipoSecundario(),
                inicial,
                ativo,
                LocalDateTime.now()
        );
    }

    public void sofrerDano(int dano) {
        hpAtual = Math.max(0, hpAtual - Math.max(1, dano));
    }

    public void curarTotalmente() {
        hpAtual = hpMaximo;
    }

    public boolean estaDerrotado() {
        return hpAtual <= 0;
    }

    public void ganharExperiencia(int valor) {
        experiencia += valor;
        while (experiencia >= experienciaParaProximoNivel()) {
            experiencia -= experienciaParaProximoNivel();
            subirNivel();
        }
    }

    public void definirAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void prepararParaBatalhaComLevelCap(int levelCap) {
        hpAtual = Math.min(hpAtual, getHpMaximoEfetivo(levelCap));
    }

    public int getNivelEfetivo(int levelCap) {
        return Math.min(nivel, levelCap);
    }

    public int getHpAtualEfetivo(int levelCap) {
        return Math.min(hpAtual, getHpMaximoEfetivo(levelCap));
    }

    public int getHpMaximoEfetivo(int levelCap) {
        return hpMaximo - (Math.max(0, nivel - getNivelEfetivo(levelCap)) * 4);
    }

    public int getAtaqueEfetivo(int levelCap) {
        return ataque - (Math.max(0, nivel - getNivelEfetivo(levelCap)) * 2);
    }

    public int getDefesaEfetiva(int levelCap) {
        return defesa - (Math.max(0, nivel - getNivelEfetivo(levelCap)) * 2);
    }

    public int getVelocidadeEfetiva(int levelCap) {
        return velocidade - (Math.max(0, nivel - getNivelEfetivo(levelCap)) * 2);
    }

    public int getExperienciaParaProximoNivel() {
        return experienciaParaProximoNivel();
    }

    private void subirNivel() {
        nivel += 1;
        hpMaximo += 4;
        ataque += 2;
        defesa += 2;
        velocidade += 2;
        hpAtual = hpMaximo;
    }

    private int experienciaParaProximoNivel() {
        return nivel * 25;
    }

    private static int calcularStat(int base, int nivel, int adicional) {
        return base / 2 + (nivel * 3) + adicional;
    }
}
