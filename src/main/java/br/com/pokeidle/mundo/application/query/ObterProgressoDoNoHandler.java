package br.com.pokeidle.mundo.application.query;

import br.com.pokeidle.mundo.domain.MissaoNo;
import br.com.pokeidle.mundo.domain.MissaoNoRepository;
import br.com.pokeidle.mundo.domain.ObjetivoMissaoNo;
import br.com.pokeidle.mundo.domain.ObjetivoMissaoNoRepository;
import br.com.pokeidle.mundo.domain.TipoObjetivoMissaoNo;
import br.com.pokeidle.progresso.domain.ProgressoNoJogador;
import br.com.pokeidle.progresso.domain.ProgressoNoJogadorRepository;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObterProgressoDoNoHandler {

    private final ProgressoNoJogadorRepository progressoNoJogadorRepository;
    private final MissaoNoRepository missaoNoRepository;
    private final ObjetivoMissaoNoRepository objetivoMissaoNoRepository;

    public ObterProgressoDoNoHandler(ProgressoNoJogadorRepository progressoNoJogadorRepository,
                                     MissaoNoRepository missaoNoRepository,
                                     ObjetivoMissaoNoRepository objetivoMissaoNoRepository) {
        this.progressoNoJogadorRepository = progressoNoJogadorRepository;
        this.missaoNoRepository = missaoNoRepository;
        this.objetivoMissaoNoRepository = objetivoMissaoNoRepository;
    }

    public ProgressoDoNoDto handle(ObterProgressoDoNoQuery query) {
        ProgressoNoJogador progresso = progressoNoJogadorRepository.findByJogadorIdAndNoJornadaId(query.jogadorId(), query.noId())
                .orElseThrow(() -> new NotFoundException("Progresso do no nao encontrado."));
        MissaoNo missao = missaoNoRepository.findByNoJornadaId(query.noId())
                .orElseThrow(() -> new NotFoundException("Missao do no nao encontrada."));
        List<ObjetivoMissaoNo> objetivos = objetivoMissaoNoRepository.findByMissaoNoIdOrderByOrdemAsc(missao.getId());
        return new ProgressoDoNoDto(
                query.noId(),
                progresso.isDesbloqueado(),
                progresso.isConcluido(),
                progresso.getBatalhasSelvagensVencidas(),
                progresso.getTreinadoresDerrotados(),
                progresso.isLiderDerrotado(),
                objetivos.stream().map(objetivo -> new ProgressoDoNoDto.ObjetivoProgressoDto(
                        objetivo.getTipoObjetivo().name(),
                        progressoAtual(progresso, objetivo.getTipoObjetivo()),
                        objetivo.getAlvoQuantidade(),
                        objetivoConcluido(progresso, objetivo),
                        objetivo.getDescricao()
                )).toList()
        );
    }

    private int progressoAtual(ProgressoNoJogador progresso, TipoObjetivoMissaoNo tipoObjetivo) {
        if (tipoObjetivo == TipoObjetivoMissaoNo.ENTRAR_NO) {
            return progresso.getBatalhasVencidas() > 0 ? 1 : 0;
        }
        if (tipoObjetivo == TipoObjetivoMissaoNo.VENCER_BATALHAS_SELVAGENS) {
            return progresso.getBatalhasSelvagensVencidas();
        }
        if (tipoObjetivo == TipoObjetivoMissaoNo.DERROTAR_TREINADORES) {
            return progresso.getTreinadoresDerrotados();
        }
        return progresso.isLiderDerrotado() ? 1 : 0;
    }

    private boolean objetivoConcluido(ProgressoNoJogador progresso, ObjetivoMissaoNo objetivo) {
        return progressoAtual(progresso, objetivo.getTipoObjetivo()) >= objetivo.getAlvoQuantidade();
    }
}
