package br.com.pokeidle.inventario.application.event;

import br.com.pokeidle.inventario.domain.CodigoItem;
import br.com.pokeidle.inventario.domain.InventarioJogador;
import br.com.pokeidle.inventario.domain.InventarioJogadorRepository;
import br.com.pokeidle.inventario.domain.ItemRepository;
import br.com.pokeidle.jogador.domain.JogadorCriadoDomainEvent;
import br.com.pokeidle.shared.infrastructure.Ids;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InicializarInventarioDoJogadorHandler {

    private final ItemRepository itemRepository;
    private final InventarioJogadorRepository inventarioJogadorRepository;

    public InicializarInventarioDoJogadorHandler(ItemRepository itemRepository,
                                                 InventarioJogadorRepository inventarioJogadorRepository) {
        this.itemRepository = itemRepository;
        this.inventarioJogadorRepository = inventarioJogadorRepository;
    }

    @EventListener
    @Transactional
    public void on(JogadorCriadoDomainEvent event) {
        itemRepository.findByCodigo(CodigoItem.POKE_BALL)
                .ifPresent(item -> inventarioJogadorRepository.save(new InventarioJogador(Ids.unique(), event.jogadorId(), item.getId(), 5)));
        itemRepository.findByCodigo(CodigoItem.POTION)
                .ifPresent(item -> inventarioJogadorRepository.save(new InventarioJogador(Ids.unique(), event.jogadorId(), item.getId(), 1)));
    }
}
