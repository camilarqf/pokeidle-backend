package br.com.pokeidle.plantel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "box_pokemon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoxPokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jogador_id", nullable = false, length = 36)
    private String jogadorId;

    @Column(name = "pokemon_capturado_id", nullable = false, length = 36)
    private String pokemonCapturadoId;

    @Column(name = "armazenado_em", nullable = false)
    private LocalDateTime armazenadoEm;

    public BoxPokemon(String jogadorId, String pokemonCapturadoId) {
        this.jogadorId = jogadorId;
        this.pokemonCapturadoId = pokemonCapturadoId;
        this.armazenadoEm = LocalDateTime.now();
    }
}
