package br.com.pokeidle.batalha.domain;

import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.shared.domain.TipoPokemon;
import br.com.pokeidle.treinadores.domain.TreinadorNpcPokemon;
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
@Table(name = "batalha_oponente_pokemon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BatalhaOponentePokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batalha_id", nullable = false, length = 36)
    private String batalhaId;

    @Column(name = "ordem_equipe", nullable = false)
    private int ordemEquipe;

    @Column(name = "especie_id", nullable = false)
    private Long especieId;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false)
    private int nivel;

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
    private boolean derrotado;

    public BatalhaOponentePokemon(String batalhaId,
                                  int ordemEquipe,
                                  Long especieId,
                                  String nome,
                                  int nivel,
                                  int hpAtual,
                                  int hpMaximo,
                                  int ataque,
                                  int defesa,
                                  int velocidade,
                                  TipoPokemon tipoPrimario,
                                  TipoPokemon tipoSecundario) {
        this.batalhaId = batalhaId;
        this.ordemEquipe = ordemEquipe;
        this.especieId = especieId;
        this.nome = nome;
        this.nivel = nivel;
        this.hpAtual = hpAtual;
        this.hpMaximo = hpMaximo;
        this.ataque = ataque;
        this.defesa = defesa;
        this.velocidade = velocidade;
        this.tipoPrimario = tipoPrimario;
        this.tipoSecundario = tipoSecundario;
        this.derrotado = false;
    }

    public static BatalhaOponentePokemon criarSelvagem(String batalhaId, PokemonEspecie especie, int nivel) {
        int hp = especie.getHpBase() / 2 + (nivel * 3) + 10;
        return new BatalhaOponentePokemon(
                batalhaId,
                0,
                especie.getId(),
                especie.getNome(),
                nivel,
                hp,
                hp,
                especie.getAtaqueBase() / 2 + (nivel * 3) + 5,
                especie.getDefesaBase() / 2 + (nivel * 3) + 5,
                especie.getVelocidadeBase() / 2 + (nivel * 3) + 5,
                especie.getTipoPrimario(),
                especie.getTipoSecundario()
        );
    }

    public static BatalhaOponentePokemon criarDeTreinador(String batalhaId, TreinadorNpcPokemon origem, PokemonEspecie especie) {
        int hp = especie.getHpBase() / 2 + (origem.getNivel() * 3) + 10;
        return new BatalhaOponentePokemon(
                batalhaId,
                origem.getOrdemEquipe(),
                especie.getId(),
                origem.getNome(),
                origem.getNivel(),
                hp,
                hp,
                especie.getAtaqueBase() / 2 + (origem.getNivel() * 3) + 5,
                especie.getDefesaBase() / 2 + (origem.getNivel() * 3) + 5,
                especie.getVelocidadeBase() / 2 + (origem.getNivel() * 3) + 5,
                especie.getTipoPrimario(),
                especie.getTipoSecundario()
        );
    }

    public void sofrerDano(int dano) {
        hpAtual = Math.max(0, hpAtual - Math.max(1, dano));
        derrotado = hpAtual == 0;
    }
}
