package br.com.pokeidle.catalogo.domain;

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
@Table(name = "pokemon_perfil_design")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PokemonPerfilDesign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pokemon_especie_id", nullable = false, unique = true)
    private Long pokemonEspecieId;

    @Column(name = "visual_key", nullable = false, length = 60)
    private String visualKey;

    @Column(name = "cor_predominante", nullable = false, length = 20)
    private String corPredominante;

    public PokemonPerfilDesign(Long pokemonEspecieId, String visualKey, String corPredominante) {
        this.pokemonEspecieId = pokemonEspecieId;
        this.visualKey = visualKey;
        this.corPredominante = corPredominante;
    }

    public void atualizar(String visualKey, String corPredominante) {
        this.visualKey = visualKey;
        this.corPredominante = corPredominante;
    }
}
