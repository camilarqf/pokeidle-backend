package br.com.pokeidle.inventario.application.query;

import br.com.pokeidle.inventario.domain.InventarioJogadorRepository;
import br.com.pokeidle.inventario.domain.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ObterInventarioHandler {

    private final InventarioJogadorRepository inventarioJogadorRepository;
    private final ItemRepository itemRepository;

    public ObterInventarioHandler(InventarioJogadorRepository inventarioJogadorRepository,
                                  ItemRepository itemRepository) {
        this.inventarioJogadorRepository = inventarioJogadorRepository;
        this.itemRepository = itemRepository;
    }

    public List<InventarioItemDto> handle(ObterInventarioQuery query) {
        var inventario = inventarioJogadorRepository.findByJogadorId(query.jogadorId());
        var itens = itemRepository.findAllByIdIn(inventario.stream().map(value -> value.getItemId()).toList())
                .stream()
                .collect(Collectors.toMap(item -> item.getId(), Function.identity()));
        return inventario.stream()
                .map(slot -> {
                    var item = itens.get(slot.getItemId());
                    return new InventarioItemDto(item.getId(), item.getCodigo().name(), item.getNome(), slot.getQuantidade());
                })
                .toList();
    }
}
