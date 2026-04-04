package br.com.pokeidle.mundo.application.query;

import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.mundo.domain.NoJornadaRepository;
import br.com.pokeidle.progresso.domain.ProgressoNoJogadorRepository;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ObterMapaAtualHandler {

    private final JogadorRepository jogadorRepository;
    private final NoJornadaRepository noJornadaRepository;
    private final ProgressoNoJogadorRepository progressoRepository;

    public ObterMapaAtualHandler(JogadorRepository jogadorRepository,
                                 NoJornadaRepository noJornadaRepository,
                                 ProgressoNoJogadorRepository progressoRepository) {
        this.jogadorRepository = jogadorRepository;
        this.noJornadaRepository = noJornadaRepository;
        this.progressoRepository = progressoRepository;
    }

    public MapaAtualDto handle(ObterMapaAtualQuery query) {
        Jogador jogador = jogadorRepository.findById(query.jogadorId())
                .orElseThrow(() -> new NotFoundException("Jogador nao encontrado."));
        Map<Long, br.com.pokeidle.progresso.domain.ProgressoNoJogador> progressos = progressoRepository.findByJogadorIdOrderByNoJornadaIdAsc(jogador.getId()).stream()
                .collect(Collectors.toMap(br.com.pokeidle.progresso.domain.ProgressoNoJogador::getNoJornadaId, Function.identity()));

        return new MapaAtualDto(
                jogador.getNoAtualId(),
                noJornadaRepository.findAllByOrderByOrdemMapaAsc().stream()
                        .map(no -> {
                            var progresso = progressos.get(no.getId());
                            return new MapaAtualDto.NoMapaDto(
                                    no.getId(),
                                    no.getNome(),
                                    no.getTipo(),
                                    progresso != null && progresso.isDesbloqueado(),
                                    progresso != null && progresso.isConcluido(),
                                    no.getId().equals(jogador.getNoAtualId())
                            );
                        })
                        .toList()
        );
    }
}
