package br.com.pokeidle.batalha.application.query;

import br.com.pokeidle.batalha.domain.BatalhaRepository;
import br.com.pokeidle.shared.domain.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ObterBatalhaAtualHandler {

    private final BatalhaRepository batalhaRepository;

    public ObterBatalhaAtualHandler(BatalhaRepository batalhaRepository) {
        this.batalhaRepository = batalhaRepository;
    }

    public BatalhaDto handle(ObterBatalhaAtualQuery query) {
        return batalhaRepository.findById(query.batalhaId())
                .map(BatalhaMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Batalha nao encontrada."));
    }
}
