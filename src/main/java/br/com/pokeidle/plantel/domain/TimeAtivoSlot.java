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

@Getter
@Entity
@Table(name = "time_ativo_slot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeAtivoSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jogador_id", nullable = false, length = 36)
    private String jogadorId;

    @Column(name = "slot_numero", nullable = false)
    private int slotNumero;

    @Column(name = "pokemon_capturado_id", nullable = false, length = 36)
    private String pokemonCapturadoId;

    public TimeAtivoSlot(String jogadorId, int slotNumero, String pokemonCapturadoId) {
        this.jogadorId = jogadorId;
        this.slotNumero = slotNumero;
        this.pokemonCapturadoId = pokemonCapturadoId;
    }

    public void trocarPokemon(String pokemonCapturadoId) {
        this.pokemonCapturadoId = pokemonCapturadoId;
    }
}
