package br.com.pokeidle.mundo.application.query;

import br.com.pokeidle.mundo.domain.MissaoNoRepository;
import br.com.pokeidle.mundo.domain.NoJornada;
import br.com.pokeidle.mundo.domain.NoJornadaRepository;
import br.com.pokeidle.mundo.domain.ObjetivoMissaoNoRepository;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ObterDetalhesDoNoHandler {

    private final NoJornadaRepository noJornadaRepository;
    private final MissaoNoRepository missaoNoRepository;
    private final ObjetivoMissaoNoRepository objetivoMissaoNoRepository;

    public ObterDetalhesDoNoHandler(NoJornadaRepository noJornadaRepository,
                                    MissaoNoRepository missaoNoRepository,
                                    ObjetivoMissaoNoRepository objetivoMissaoNoRepository) {
        this.noJornadaRepository = noJornadaRepository;
        this.missaoNoRepository = missaoNoRepository;
        this.objetivoMissaoNoRepository = objetivoMissaoNoRepository;
    }

    public DetalhesNoDto handle(ObterDetalhesDoNoQuery query) {
        NoJornada no = noJornadaRepository.findById(query.noId())
                .orElseThrow(() -> new NotFoundException("No nao encontrado."));

        var missao = missaoNoRepository.findByNoJornadaId(no.getId())
                .map(value -> new DetalhesNoDto.MissaoDto(
                        value.getTipoMissao().name(),
                        value.getAlvoQuantidade(),
                        value.getDescricao(),
                        objetivoMissaoNoRepository.findByMissaoNoIdOrderByOrdemAsc(value.getId()).stream()
                                .map(objetivo -> new DetalhesNoDto.ObjetivoDto(
                                        objetivo.getTipoObjetivo().name(),
                                        objetivo.getAlvoQuantidade(),
                                        objetivo.getDescricao(),
                                        objetivo.getOrdem()
                                ))
                                .toList()
                ))
                .orElse(null);

        return new DetalhesNoDto(
                no.getId(),
                no.getNome(),
                no.getDescricao(),
                no.getTipo(),
                no.isPermiteBatalhaSelvagem(),
                no.isCidadeHub(),
                missao
        );
    }
}
