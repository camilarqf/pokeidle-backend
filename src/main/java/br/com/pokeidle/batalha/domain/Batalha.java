package br.com.pokeidle.batalha.domain;

import br.com.pokeidle.captura.domain.PokemonCapturadoDomainEvent;
import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
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
@Table(name = "batalha")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Batalha extends AggregateRoot {

    @Id
    private String id;

    @Column(name = "jogador_id", nullable = false, length = 36)
    private String jogadorId;

    @Column(name = "no_jornada_id", nullable = false)
    private Long noJornadaId;

    @Column(name = "pokemon_jogador_id", nullable = false, length = 36)
    private String pokemonJogadorId;

    @Column(name = "especie_selvagem_id", nullable = false)
    private Long especieSelvagemId;

    @Column(name = "nome_selvagem", nullable = false, length = 80)
    private String nomeSelvagem;

    @Column(name = "nivel_selvagem", nullable = false)
    private int nivelSelvagem;

    @Column(name = "hp_atual_selvagem", nullable = false)
    private int hpAtualSelvagem;

    @Column(name = "hp_maximo_selvagem", nullable = false)
    private int hpMaximoSelvagem;

    @Column(name = "ataque_selvagem", nullable = false)
    private int ataqueSelvagem;

    @Column(name = "defesa_selvagem", nullable = false)
    private int defesaSelvagem;

    @Column(name = "velocidade_selvagem", nullable = false)
    private int velocidadeSelvagem;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_primario_selvagem", nullable = false, length = 20)
    private TipoPokemon tipoPrimarioSelvagem;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_secundario_selvagem", length = 20)
    private TipoPokemon tipoSecundarioSelvagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusBatalha status;

    @Column(nullable = false)
    private int turnos;

    @Column(name = "experiencia_concedida", nullable = false)
    private int experienciaConcedida;

    @Column(name = "criada_em", nullable = false)
    private LocalDateTime criadaEm;

    private Batalha(String id,
                    String jogadorId,
                    Long noJornadaId,
                    String pokemonJogadorId,
                    Long especieSelvagemId,
                    String nomeSelvagem,
                    int nivelSelvagem,
                    int hpAtualSelvagem,
                    int hpMaximoSelvagem,
                    int ataqueSelvagem,
                    int defesaSelvagem,
                    int velocidadeSelvagem,
                    TipoPokemon tipoPrimarioSelvagem,
                    TipoPokemon tipoSecundarioSelvagem,
                    StatusBatalha status,
                    int turnos,
                    int experienciaConcedida,
                    LocalDateTime criadaEm) {
        this.id = id;
        this.jogadorId = jogadorId;
        this.noJornadaId = noJornadaId;
        this.pokemonJogadorId = pokemonJogadorId;
        this.especieSelvagemId = especieSelvagemId;
        this.nomeSelvagem = nomeSelvagem;
        this.nivelSelvagem = nivelSelvagem;
        this.hpAtualSelvagem = hpAtualSelvagem;
        this.hpMaximoSelvagem = hpMaximoSelvagem;
        this.ataqueSelvagem = ataqueSelvagem;
        this.defesaSelvagem = defesaSelvagem;
        this.velocidadeSelvagem = velocidadeSelvagem;
        this.tipoPrimarioSelvagem = tipoPrimarioSelvagem;
        this.tipoSecundarioSelvagem = tipoSecundarioSelvagem;
        this.status = status;
        this.turnos = turnos;
        this.experienciaConcedida = experienciaConcedida;
        this.criadaEm = criadaEm;
    }

    public static Batalha criarSelvagem(String id, String jogadorId, Long noJornadaId, PokemonCapturado pokemonJogador, PokemonEspecie especieSelvagem, int nivelSelvagem) {
        int hp = especieSelvagem.getHpBase() / 2 + (nivelSelvagem * 3) + 10;
        int ataque = especieSelvagem.getAtaqueBase() / 2 + (nivelSelvagem * 3) + 5;
        int defesa = especieSelvagem.getDefesaBase() / 2 + (nivelSelvagem * 3) + 5;
        int velocidade = especieSelvagem.getVelocidadeBase() / 2 + (nivelSelvagem * 3) + 5;
        int xp = 20 + (nivelSelvagem * 10);
        return new Batalha(
                id,
                jogadorId,
                noJornadaId,
                pokemonJogador.getId(),
                especieSelvagem.getId(),
                especieSelvagem.getNome(),
                nivelSelvagem,
                hp,
                hp,
                ataque,
                defesa,
                velocidade,
                especieSelvagem.getTipoPrimario(),
                especieSelvagem.getTipoSecundario(),
                StatusBatalha.EM_ANDAMENTO,
                0,
                xp,
                LocalDateTime.now()
        );
    }

    public void resolverTurno(PokemonCapturado pokemonJogador) {
        validarEmAndamento();
        turnos += 1;

        if (pokemonJogador.getVelocidade() >= velocidadeSelvagem) {
            atacarJogadorParaSelvagem(pokemonJogador);
            if (status == StatusBatalha.VITORIA) {
                return;
            }
            atacarSelvagemParaJogador(pokemonJogador);
            return;
        }

        atacarSelvagemParaJogador(pokemonJogador);
        if (status == StatusBatalha.DERROTA) {
            return;
        }
        atacarJogadorParaSelvagem(pokemonJogador);
    }

    public void registrarCaptura(String pokemonCapturadoId) {
        validarEmAndamento();
        this.status = StatusBatalha.CAPTURADA;
        registerEvent(new PokemonCapturadoDomainEvent(id, jogadorId, especieSelvagemId, pokemonCapturadoId));
    }

    public boolean estaEmAndamento() {
        return status == StatusBatalha.EM_ANDAMENTO;
    }

    private void atacarJogadorParaSelvagem(PokemonCapturado pokemonJogador) {
        int dano = CalculadoraDano.calcular(pokemonJogador.getAtaque(), defesaSelvagem, pokemonJogador.getTipoPrimario(), tipoPrimarioSelvagem);
        hpAtualSelvagem = Math.max(0, hpAtualSelvagem - dano);
        if (hpAtualSelvagem == 0) {
            status = StatusBatalha.VITORIA;
            registerEvent(new BatalhaSelvagemVencidaDomainEvent(id, jogadorId, noJornadaId, pokemonJogadorId, experienciaConcedida));
        }
    }

    private void atacarSelvagemParaJogador(PokemonCapturado pokemonJogador) {
        int dano = CalculadoraDano.calcular(ataqueSelvagem, pokemonJogador.getDefesa(), tipoPrimarioSelvagem, pokemonJogador.getTipoPrimario());
        pokemonJogador.sofrerDano(dano);
        if (pokemonJogador.estaDerrotado()) {
            status = StatusBatalha.DERROTA;
        }
    }

    private void validarEmAndamento() {
        if (!estaEmAndamento()) {
            throw new IllegalStateException("A batalha nao esta mais em andamento.");
        }
    }
}
