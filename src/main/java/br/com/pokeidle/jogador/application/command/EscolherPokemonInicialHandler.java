package br.com.pokeidle.jogador.application.command;

import br.com.pokeidle.catalogo.domain.PokemonEspecie;
import br.com.pokeidle.catalogo.domain.PokemonEspecieRepository;
import br.com.pokeidle.jogador.application.query.JogadorDto;
import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.plantel.domain.PokemonCapturado;
import br.com.pokeidle.plantel.domain.PokemonCapturadoRepository;
import br.com.pokeidle.shared.application.DomainEventPublisher;
import br.com.pokeidle.shared.domain.BusinessException;
import br.com.pokeidle.shared.domain.NotFoundException;
import br.com.pokeidle.shared.infrastructure.Ids;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class EscolherPokemonInicialHandler {

    private static final Set<String> STARTERS_V0 = Set.of("bulbasaur", "charmander", "squirtle");

    private final JogadorRepository jogadorRepository;
    private final PokemonEspecieRepository pokemonEspecieRepository;
    private final PokemonCapturadoRepository pokemonCapturadoRepository;
    private final DomainEventPublisher domainEventPublisher;

    public EscolherPokemonInicialHandler(JogadorRepository jogadorRepository,
                                         PokemonEspecieRepository pokemonEspecieRepository,
                                         PokemonCapturadoRepository pokemonCapturadoRepository,
                                         DomainEventPublisher domainEventPublisher) {
        this.jogadorRepository = jogadorRepository;
        this.pokemonEspecieRepository = pokemonEspecieRepository;
        this.pokemonCapturadoRepository = pokemonCapturadoRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional
    public JogadorDto handle(EscolherPokemonInicialCommand command) {
        Jogador jogador = jogadorRepository.findById(command.jogadorId())
                .orElseThrow(() -> new NotFoundException("Jogador nao encontrado."));
        PokemonEspecie especie = pokemonEspecieRepository.findById(command.pokemonEspecieId())
                .orElseThrow(() -> new NotFoundException("Especie nao encontrada no catalogo local."));
        if (!STARTERS_V0.contains(especie.getNome().toLowerCase())) {
            throw new BusinessException("No v0, apenas Bulbasaur, Charmander e Squirtle podem ser escolhidos como inicial.");
        }

        PokemonCapturado pokemonInicial = PokemonCapturado.criarInicial(Ids.unique(), jogador.getId(), especie);
        pokemonCapturadoRepository.save(pokemonInicial);
        jogador.escolherPokemonInicial(pokemonInicial.getId());
        jogadorRepository.save(jogador);
        domainEventPublisher.publishAll(jogador.pullDomainEvents());
        return new JogadorDto(jogador.getId(), jogador.getNomePerfil(), jogador.getSaldoMoedas(), jogador.getNoAtualId(), jogador.isPokemonInicialEscolhido());
    }
}
