package br.com.pokeidle.progresso.application.command;

import br.com.pokeidle.mundo.domain.MissaoNo;
import br.com.pokeidle.mundo.domain.MissaoNoRepository;
import br.com.pokeidle.progresso.domain.ProgressoNoJogador;
import br.com.pokeidle.progresso.domain.ProgressoNoJogadorRepository;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtualizarProgressoDoNoHandler {

    private final ProgressoNoJogadorRepository progressoRepository;
    private final MissaoNoRepository missaoNoRepository;

    public AtualizarProgressoDoNoHandler(ProgressoNoJogadorRepository progressoRepository,
                                         MissaoNoRepository missaoNoRepository) {
        this.progressoRepository = progressoRepository;
        this.missaoNoRepository = missaoNoRepository;
    }

    @Transactional
    public ProgressoNoJogador handle(AtualizarProgressoDoNoCommand command) {
        ProgressoNoJogador progresso = progressoRepository.findByJogadorIdAndNoJornadaId(command.jogadorId(), command.noId())
                .orElseThrow(() -> new NotFoundException("Progresso do no nao encontrado."));
        MissaoNo missao = missaoNoRepository.findByNoJornadaId(command.noId())
                .orElseThrow(() -> new NotFoundException("Missao do no nao encontrada."));
        progresso.registrarVitorias(command.vitoriasObtidas(), missao.getAlvoQuantidade());
        return progressoRepository.save(progresso);
    }
}
