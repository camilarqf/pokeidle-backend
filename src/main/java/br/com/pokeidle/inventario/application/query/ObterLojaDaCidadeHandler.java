package br.com.pokeidle.inventario.application.query;

import br.com.pokeidle.inventario.domain.CidadeItemLojaRepository;
import br.com.pokeidle.inventario.domain.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ObterLojaDaCidadeHandler {

    private final CidadeItemLojaRepository cidadeItemLojaRepository;
    private final ItemRepository itemRepository;

    public ObterLojaDaCidadeHandler(CidadeItemLojaRepository cidadeItemLojaRepository,
                                    ItemRepository itemRepository) {
        this.cidadeItemLojaRepository = cidadeItemLojaRepository;
        this.itemRepository = itemRepository;
    }

    public java.util.List<LojaItemDto> handle(ObterLojaDaCidadeQuery query) {
        var itensLoja = cidadeItemLojaRepository.findByCidadeId(query.cidadeId());
        var itens = itemRepository.findAllByIdIn(itensLoja.stream().map(value -> value.getItemId()).toList())
                .stream()
                .collect(Collectors.toMap(item -> item.getId(), Function.identity()));
        return itensLoja.stream()
                .map(registro -> {
                    var item = itens.get(registro.getItemId());
                    return new LojaItemDto(item.getId(), item.getCodigo().name(), item.getNome(), registro.getPreco());
                })
                .toList();
    }
}
