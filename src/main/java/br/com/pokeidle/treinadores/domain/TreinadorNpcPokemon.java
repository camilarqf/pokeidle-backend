package br.com.pokeidle.treinadores.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "treinador_npc_pokemon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TreinadorNpcPokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "treinador_npc_id", nullable = false)
    private Long treinadorNpcId;

    @Column(name = "especie_id", nullable = false)
    private Long especieId;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = false)
    private int nivel;

    @Column(name = "ordem_equipe", nullable = false)
    private int ordemEquipe;
}
