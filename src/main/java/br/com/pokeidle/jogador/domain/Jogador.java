package br.com.pokeidle.jogador.domain;

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
@Table(name = "jogador")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Jogador extends AggregateRoot {

    @Id
    private String id;

    @Column(name = "nome_perfil", nullable = false, length = 80, unique = true)
    private String nomePerfil;

    @Column(name = "saldo_moedas", nullable = false)
    private int saldoMoedas;

    @Column(name = "no_atual_id")
    private Long noAtualId;

    @Column(name = "pokemon_inicial_escolhido", nullable = false)
    private boolean pokemonInicialEscolhido;

    @Column(name = "nivel_cap_atual", nullable = false)
    private int nivelCapAtual;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    private Jogador(String id,
                    String nomePerfil,
                    int saldoMoedas,
                    Long noAtualId,
                    boolean pokemonInicialEscolhido,
                    int nivelCapAtual,
                    LocalDateTime criadoEm) {
        this.id = id;
        this.nomePerfil = nomePerfil;
        this.saldoMoedas = saldoMoedas;
        this.noAtualId = noAtualId;
        this.pokemonInicialEscolhido = pokemonInicialEscolhido;
        this.nivelCapAtual = nivelCapAtual;
        this.criadoEm = criadoEm;
    }

    public static Jogador criar(String id, String nomePerfil, Long noInicialId) {
        Jogador jogador = new Jogador(id, nomePerfil, 500, noInicialId, false, 12, LocalDateTime.now());
        jogador.registerEvent(new JogadorCriadoDomainEvent(id));
        return jogador;
    }

    public void escolherPokemonInicial(String pokemonCapturadoId) {
        if (pokemonInicialEscolhido) {
            throw new IllegalStateException("O jogador ja escolheu um pokemon inicial.");
        }
        this.pokemonInicialEscolhido = true;
        registerEvent(new PokemonInicialEscolhidoDomainEvent(id, pokemonCapturadoId));
    }

    public void entrarNo(Long noId) {
        this.noAtualId = noId;
    }

    public void creditarMoedas(int valor) {
        this.saldoMoedas += valor;
    }

    public void debitarMoedas(int valor) {
        if (valor > saldoMoedas) {
            throw new IllegalStateException("Saldo insuficiente.");
        }
        this.saldoMoedas -= valor;
    }

    public void aumentarLevelCap(int novoCap) {
        if (novoCap <= nivelCapAtual) {
            return;
        }
        this.nivelCapAtual = novoCap;
        registerEvent(new LevelCapAumentadoDomainEvent(id, novoCap));
    }
}
