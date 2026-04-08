package br.com.pokeidle.treinadores.application.query;

import br.com.pokeidle.jogador.domain.BadgeJogadorRepository;
import br.com.pokeidle.jogador.domain.CodigoBadge;
import br.com.pokeidle.shared.domain.NotFoundException;
import br.com.pokeidle.treinadores.domain.Ginasio;
import br.com.pokeidle.treinadores.domain.GinasioRepository;
import br.com.pokeidle.treinadores.domain.JogadorTreinadorDerrotadoRepository;
import br.com.pokeidle.treinadores.domain.TreinadorNpcRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ObterStatusDoGinasioHandler {

    private final GinasioRepository ginasioRepository;
    private final TreinadorNpcRepository treinadorNpcRepository;
    private final JogadorTreinadorDerrotadoRepository jogadorTreinadorDerrotadoRepository;
    private final BadgeJogadorRepository badgeJogadorRepository;

    public ObterStatusDoGinasioHandler(GinasioRepository ginasioRepository,
                                       TreinadorNpcRepository treinadorNpcRepository,
                                       JogadorTreinadorDerrotadoRepository jogadorTreinadorDerrotadoRepository,
                                       BadgeJogadorRepository badgeJogadorRepository) {
        this.ginasioRepository = ginasioRepository;
        this.treinadorNpcRepository = treinadorNpcRepository;
        this.jogadorTreinadorDerrotadoRepository = jogadorTreinadorDerrotadoRepository;
        this.badgeJogadorRepository = badgeJogadorRepository;
    }

    public StatusDoGinasioDto handle(ObterStatusDoGinasioQuery query) {
        Ginasio ginasio = ginasioRepository.findById(query.ginasioId())
                .orElseThrow(() -> new NotFoundException("Ginasio nao encontrado."));
        var treinadores = treinadorNpcRepository.findByGinasioIdOrderByOrdemDesafioAsc(ginasio.getId());
        Set<Long> derrotados = jogadorTreinadorDerrotadoRepository.findByJogadorId(query.jogadorId()).stream()
                .map(registro -> registro.getTreinadorNpcId())
                .collect(Collectors.toSet());
        boolean liderDisponivel = treinadores.stream()
                .filter(treinador -> !treinador.isLider())
                .allMatch(treinador -> derrotados.contains(treinador.getId()));
        boolean badgeObtida = badgeJogadorRepository.findByJogadorIdAndCodigo(query.jogadorId(), CodigoBadge.valueOf(ginasio.getBadgeCodigo())).isPresent();
        return new StatusDoGinasioDto(
                ginasio.getId(),
                ginasio.getNome(),
                badgeObtida,
                liderDisponivel,
                treinadores.stream()
                        .map(treinador -> new StatusDoGinasioDto.TreinadorStatusDto(
                                treinador.getId(),
                                treinador.getNome(),
                                treinador.isLider(),
                                derrotados.contains(treinador.getId()),
                                treinador.getOrdemDesafio()
                        ))
                        .toList()
        );
    }
}
