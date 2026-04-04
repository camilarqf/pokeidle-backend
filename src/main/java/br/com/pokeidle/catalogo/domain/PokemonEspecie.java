package br.com.pokeidle.catalogo.domain;

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

@Getter
@Entity
@Table(name = "pokemon_especie")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PokemonEspecie {

    @Id
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_primario", nullable = false, length = 20)
    private TipoPokemon tipoPrimario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_secundario", length = 20)
    private TipoPokemon tipoSecundario;

    @Column(name = "hp_base", nullable = false)
    private int hpBase;

    @Column(name = "ataque_base", nullable = false)
    private int ataqueBase;

    @Column(name = "defesa_base", nullable = false)
    private int defesaBase;

    @Column(name = "velocidade_base", nullable = false)
    private int velocidadeBase;

    @Column(nullable = false)
    private int altura;

    @Column(nullable = false)
    private int peso;

    @Column(name = "sprite_principal", length = 500)
    private String spritePrincipal;

    @Column(name = "taxa_captura", nullable = false)
    private int taxaCaptura;

    @Column(nullable = false, length = 60)
    private String geracao;

    public PokemonEspecie(Long id,
                          String nome,
                          TipoPokemon tipoPrimario,
                          TipoPokemon tipoSecundario,
                          int hpBase,
                          int ataqueBase,
                          int defesaBase,
                          int velocidadeBase,
                          int altura,
                          int peso,
                          String spritePrincipal,
                          int taxaCaptura,
                          String geracao) {
        this.id = id;
        atualizarImportacao(nome, tipoPrimario, tipoSecundario, hpBase, ataqueBase, defesaBase, velocidadeBase, altura, peso, spritePrincipal, taxaCaptura, geracao);
    }

    public void atualizarImportacao(String nome,
                                    TipoPokemon tipoPrimario,
                                    TipoPokemon tipoSecundario,
                                    int hpBase,
                                    int ataqueBase,
                                    int defesaBase,
                                    int velocidadeBase,
                                    int altura,
                                    int peso,
                                    String spritePrincipal,
                                    int taxaCaptura,
                                    String geracao) {
        this.nome = nome;
        this.tipoPrimario = tipoPrimario;
        this.tipoSecundario = tipoSecundario;
        this.hpBase = hpBase;
        this.ataqueBase = ataqueBase;
        this.defesaBase = defesaBase;
        this.velocidadeBase = velocidadeBase;
        this.altura = altura;
        this.peso = peso;
        this.spritePrincipal = spritePrincipal;
        this.taxaCaptura = taxaCaptura;
        this.geracao = geracao;
    }
}
