package br.com.pokeidle.treinadores.application.query;

import br.com.pokeidle.shared.domain.NotFoundException;
import br.com.pokeidle.treinadores.domain.GinasioRepository;
import org.springframework.stereotype.Service;

@Service
public class ObterGinasioHandler {

    private final GinasioRepository ginasioRepository;

    public ObterGinasioHandler(GinasioRepository ginasioRepository) {
        this.ginasioRepository = ginasioRepository;
    }

    public GinasioDto handle(Long ginasioId) {
        return ginasioRepository.findById(ginasioId)
                .map(ginasio -> new GinasioDto(
                        ginasio.getId(),
                        ginasio.getNome(),
                        ginasio.getCidadeId(),
                        ginasio.getNoJornadaId(),
                        ginasio.getBadgeCodigo(),
                        ginasio.getBadgeNome(),
                        ginasio.getLiderTreinadorId()
                ))
                .orElseThrow(() -> new NotFoundException("Ginasio nao encontrado."));
    }
}
