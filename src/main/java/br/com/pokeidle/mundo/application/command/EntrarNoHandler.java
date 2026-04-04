package br.com.pokeidle.mundo.application.command;

import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.mundo.domain.NoJornada;
import br.com.pokeidle.mundo.domain.NoJornadaRepository;
import br.com.pokeidle.progresso.domain.ProgressoNoJogadorRepository;
import br.com.pokeidle.shared.domain.BusinessException;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EntrarNoHandler {

    private final JogadorRepository jogadorRepository;
    private final NoJornadaRepository noJornadaRepository;
    private final ProgressoNoJogadorRepository progressoRepository;

    public EntrarNoHandler(JogadorRepository jogadorRepository,
                           NoJornadaRepository noJornadaRepository,
                           ProgressoNoJogadorRepository progressoRepository) {
        this.jogadorRepository = jogadorRepository;
        this.noJornadaRepository = noJornadaRepository;
        this.progressoRepository = progressoRepository;
    }

    @Transactional
    public void handle(EntrarNoCommand command) {
        Jogador jogador = jogadorRepository.findById(command.jogadorId())
                .orElseThrow(() -> new NotFoundException("Jogador nao encontrado."));
        NoJornada no = noJornadaRepository.findById(command.noId())
                .orElseThrow(() -> new NotFoundException("No nao encontrado."));
        boolean desbloqueado = progressoRepository.findByJogadorIdAndNoJornadaId(jogador.getId(), no.getId())
                .map(progresso -> progresso.isDesbloqueado())
                .orElse(false);
        if (!desbloqueado) {
            throw new BusinessException("Esse no ainda nao foi desbloqueado.");
        }
        jogador.entrarNo(no.getId());
        jogadorRepository.save(jogador);
    }
}
