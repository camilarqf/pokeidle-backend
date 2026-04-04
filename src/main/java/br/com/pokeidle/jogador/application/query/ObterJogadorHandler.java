package br.com.pokeidle.jogador.application.query;

import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ObterJogadorHandler {

    private final JogadorRepository jogadorRepository;

    public ObterJogadorHandler(JogadorRepository jogadorRepository) {
        this.jogadorRepository = jogadorRepository;
    }

    public JogadorDto handle(String jogadorId) {
        Jogador jogador = jogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new NotFoundException("Jogador nao encontrado."));
        return new JogadorDto(jogador.getId(), jogador.getNomePerfil(), jogador.getSaldoMoedas(), jogador.getNoAtualId(), jogador.isPokemonInicialEscolhido());
    }
}
