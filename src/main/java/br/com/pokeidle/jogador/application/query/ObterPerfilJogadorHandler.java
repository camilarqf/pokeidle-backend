package br.com.pokeidle.jogador.application.query;

import br.com.pokeidle.jogador.domain.BadgeJogadorRepository;
import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ObterPerfilJogadorHandler {

    private final JogadorRepository jogadorRepository;
    private final BadgeJogadorRepository badgeJogadorRepository;

    public ObterPerfilJogadorHandler(JogadorRepository jogadorRepository,
                                     BadgeJogadorRepository badgeJogadorRepository) {
        this.jogadorRepository = jogadorRepository;
        this.badgeJogadorRepository = badgeJogadorRepository;
    }

    public JogadorDto handle(ObterPerfilJogadorQuery query) {
        Jogador jogador = jogadorRepository.findById(query.jogadorId())
                .orElseThrow(() -> new NotFoundException("Jogador nao encontrado."));
        return new JogadorDto(
                jogador.getId(),
                jogador.getNomePerfil(),
                jogador.getSaldoMoedas(),
                jogador.getNoAtualId(),
                jogador.isPokemonInicialEscolhido(),
                jogador.getNivelCapAtual(),
                badgeJogadorRepository.findByJogadorIdOrderByObtidaEmAsc(jogador.getId()).stream()
                        .map(badge -> badge.getNome())
                        .toList()
        );
    }
}
