package br.com.pokeidle.batalha.domain;

import br.com.pokeidle.captura.domain.PokemonCapturadoDomainEvent;
import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.shared.domain.AggregateRoot;
import br.com.pokeidle.shared.domain.TipoPokemon;
import br.com.pokeidle.treinadores.domain.LiderDeGinasioDerrotadoDomainEvent;
import br.com.pokeidle.treinadores.domain.TreinadorNpc;
import br.com.pokeidle.treinadores.domain.TreinadorNpcDerrotadoDomainEvent;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoBatalha tipo;

    @Column(name = "treinador_npc_id")
    private Long treinadorNpcId;

    @Column(name = "nome_oponente", length = 80)
    private String nomeOponente;

    @Column(name = "recompensa_moedas", nullable = false)
    private int recompensaMoedas;

    @Column(name = "captura_permitida", nullable = false)
    private boolean capturaPermitida;

    @Column(name = "indice_oponente_atual", nullable = false)
    private int indiceOponenteAtual;

    @Column(name = "concluida_primeira_vez", nullable = false)
    private boolean concluidaPrimeiraVez;

    private Batalha(String id,
                    String jogadorId,
                    Long noJornadaId,
                    String pokemonJogadorId,
                    int experienciaConcedida,
                    TipoBatalha tipo,
                    Long treinadorNpcId,
                    String nomeOponente,
                    int recompensaMoedas,
                    boolean capturaPermitida,
                    boolean concluidaPrimeiraVez,
                    LocalDateTime criadaEm) {
        this.id = id;
        this.jogadorId = jogadorId;
        this.noJornadaId = noJornadaId;
        this.pokemonJogadorId = pokemonJogadorId;
        this.status = StatusBatalha.EM_ANDAMENTO;
        this.turnos = 0;
        this.experienciaConcedida = experienciaConcedida;
        this.criadaEm = criadaEm;
        this.tipo = tipo;
        this.treinadorNpcId = treinadorNpcId;
        this.nomeOponente = nomeOponente;
        this.recompensaMoedas = recompensaMoedas;
        this.capturaPermitida = capturaPermitida;
        this.indiceOponenteAtual = 0;
        this.concluidaPrimeiraVez = concluidaPrimeiraVez;
    }

    public static Batalha criarSelvagem(String id,
                                        String jogadorId,
                                        Long noJornadaId,
                                        PokemonCapturado pokemonJogador,
                                        PokemonEspecie especieSelvagem,
                                        int nivelSelvagem) {
        Batalha batalha = new Batalha(
                id,
                jogadorId,
                noJornadaId,
                pokemonJogador.getId(),
                20 + (nivelSelvagem * 10),
                TipoBatalha.SELVAGEM,
                null,
                especieSelvagem.getNome(),
                0,
                true,
                false,
                LocalDateTime.now()
        );
        batalha.sincronizarOponenteAtual(BatalhaOponentePokemon.criarSelvagem(id, especieSelvagem, nivelSelvagem));
        return batalha;
    }

    public static Batalha criarContraTreinador(String id,
                                               String jogadorId,
                                               Long noJornadaId,
                                               PokemonCapturado pokemonJogador,
                                               TreinadorNpc treinador,
                                               BatalhaOponentePokemon primeiroOponente) {
        Batalha batalha = new Batalha(
                id,
                jogadorId,
                noJornadaId,
                pokemonJogador.getId(),
                treinador.getExperienciaRecompensa(),
                treinador.isLider() ? TipoBatalha.LIDER : TipoBatalha.TREINADOR,
                treinador.getId(),
                treinador.getNome(),
                treinador.getRecompensaMoedas(),
                false,
                true,
                LocalDateTime.now()
        );
        batalha.indiceOponenteAtual = primeiroOponente.getOrdemEquipe();
        batalha.sincronizarOponenteAtual(primeiroOponente);
        return batalha;
    }

    public void resolverTurno(PokemonCapturado pokemonJogador, BatalhaOponentePokemon oponenteAtual, int levelCapJogador) {
        validarEmAndamento();
        pokemonJogador.prepararParaBatalhaComLevelCap(levelCapJogador);
        turnos += 1;

        if (pokemonJogador.getVelocidadeEfetiva(levelCapJogador) >= oponenteAtual.getVelocidade()) {
            atacarJogadorParaOponente(pokemonJogador, oponenteAtual, levelCapJogador);
            if (oponenteAtual.isDerrotado()) {
                return;
            }
            atacarOponenteParaJogador(pokemonJogador, oponenteAtual, levelCapJogador);
            return;
        }

        atacarOponenteParaJogador(pokemonJogador, oponenteAtual, levelCapJogador);
        if (pokemonJogador.estaDerrotado()) {
            return;
        }
        atacarJogadorParaOponente(pokemonJogador, oponenteAtual, levelCapJogador);
    }

    public void trocarPokemonJogador(String pokemonJogadorId) {
        this.pokemonJogadorId = pokemonJogadorId;
    }

    public void avancarOponente(BatalhaOponentePokemon proximoOponente) {
        this.indiceOponenteAtual = proximoOponente.getOrdemEquipe();
        sincronizarOponenteAtual(proximoOponente);
    }

    public void registrarVitoriaFinal() {
        status = StatusBatalha.VITORIA;
        if (tipo == TipoBatalha.SELVAGEM) {
            registerEvent(new BatalhaSelvagemVencidaDomainEvent(id, jogadorId, noJornadaId, pokemonJogadorId, experienciaConcedida));
            return;
        }
        if (tipo == TipoBatalha.LIDER) {
            registerEvent(new LiderDeGinasioDerrotadoDomainEvent(jogadorId, treinadorNpcId, treinadorNpcId, noJornadaId, recompensaMoedas, experienciaConcedida, pokemonJogadorId));
            return;
        }
        registerEvent(new TreinadorNpcDerrotadoDomainEvent(jogadorId, treinadorNpcId, noJornadaId, recompensaMoedas, experienciaConcedida, pokemonJogadorId));
    }

    public void registrarDerrota() {
        status = StatusBatalha.DERROTA;
    }

    public void registrarCaptura(String pokemonCapturadoId) {
        validarEmAndamento();
        if (!capturaPermitida || tipo != TipoBatalha.SELVAGEM) {
            throw new IllegalStateException("Captura indisponivel para este tipo de batalha.");
        }
        this.status = StatusBatalha.CAPTURADA;
        registerEvent(new PokemonCapturadoDomainEvent(id, jogadorId, especieSelvagemId, pokemonCapturadoId));
    }

    public boolean estaEmAndamento() {
        return status == StatusBatalha.EM_ANDAMENTO;
    }

    public boolean oponenteAtualFoiDerrotado() {
        return hpAtualSelvagem == 0;
    }

    private void atacarJogadorParaOponente(PokemonCapturado pokemonJogador,
                                           BatalhaOponentePokemon oponenteAtual,
                                           int levelCapJogador) {
        int dano = CalculadoraDano.calcular(
                pokemonJogador.getAtaqueEfetivo(levelCapJogador),
                oponenteAtual.getDefesa(),
                pokemonJogador.getTipoPrimario(),
                oponenteAtual.getTipoPrimario()
        );
        oponenteAtual.sofrerDano(dano);
        sincronizarOponenteAtual(oponenteAtual);
    }

    private void atacarOponenteParaJogador(PokemonCapturado pokemonJogador,
                                           BatalhaOponentePokemon oponenteAtual,
                                           int levelCapJogador) {
        int dano = CalculadoraDano.calcular(
                oponenteAtual.getAtaque(),
                pokemonJogador.getDefesaEfetiva(levelCapJogador),
                oponenteAtual.getTipoPrimario(),
                pokemonJogador.getTipoPrimario()
        );
        pokemonJogador.sofrerDano(dano);
    }

    private void sincronizarOponenteAtual(BatalhaOponentePokemon oponenteAtual) {
        especieSelvagemId = oponenteAtual.getEspecieId();
        nomeSelvagem = oponenteAtual.getNome();
        nivelSelvagem = oponenteAtual.getNivel();
        hpAtualSelvagem = oponenteAtual.getHpAtual();
        hpMaximoSelvagem = oponenteAtual.getHpMaximo();
        ataqueSelvagem = oponenteAtual.getAtaque();
        defesaSelvagem = oponenteAtual.getDefesa();
        velocidadeSelvagem = oponenteAtual.getVelocidade();
        tipoPrimarioSelvagem = oponenteAtual.getTipoPrimario();
        tipoSecundarioSelvagem = oponenteAtual.getTipoSecundario();
    }

    private void validarEmAndamento() {
        if (!estaEmAndamento()) {
            throw new IllegalStateException("A batalha nao esta mais em andamento.");
        }
    }
}
