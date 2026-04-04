package br.com.pokeidle.progresso.application.event;

import br.com.pokeidle.jogador.domain.JogadorCriadoDomainEvent;
import br.com.pokeidle.mundo.domain.NoJornada;
import br.com.pokeidle.mundo.domain.NoJornadaRepository;
import br.com.pokeidle.progresso.domain.ProgressoNoJogador;
import br.com.pokeidle.progresso.domain.ProgressoNoJogadorRepository;
import br.com.pokeidle.shared.infrastructure.Ids;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InicializarProgressoJogadorHandler {

    private final NoJornadaRepository noJornadaRepository;
    private final ProgressoNoJogadorRepository progressoRepository;

    public InicializarProgressoJogadorHandler(NoJornadaRepository noJornadaRepository,
                                              ProgressoNoJogadorRepository progressoRepository) {
        this.noJornadaRepository = noJornadaRepository;
        this.progressoRepository = progressoRepository;
    }

    @EventListener
    @Transactional
    public void on(JogadorCriadoDomainEvent event) {
        for (NoJornada no : noJornadaRepository.findAllByOrderByOrdemMapaAsc()) {
            progressoRepository.save(new ProgressoNoJogador(
                    Ids.unique(),
                    event.jogadorId(),
                    no.getId(),
                    no.isDesbloqueadoInicial()
            ));
        }
    }
}
