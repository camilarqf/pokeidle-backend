package br.com.pokeidle.inventario.application.command;

import br.com.pokeidle.inventario.domain.CidadeItemLoja;
import br.com.pokeidle.inventario.domain.CidadeItemLojaRepository;
import br.com.pokeidle.inventario.domain.InventarioJogador;
import br.com.pokeidle.inventario.domain.InventarioJogadorRepository;
import br.com.pokeidle.inventario.domain.Item;
import br.com.pokeidle.inventario.domain.ItemRepository;
import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.mundo.domain.NoJornada;
import br.com.pokeidle.mundo.domain.NoJornadaRepository;
import br.com.pokeidle.shared.domain.BusinessException;
import br.com.pokeidle.shared.domain.NotFoundException;
import br.com.pokeidle.shared.infrastructure.Ids;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComprarItemHandler {

    private final JogadorRepository jogadorRepository;
    private final NoJornadaRepository noJornadaRepository;
    private final CidadeItemLojaRepository cidadeItemLojaRepository;
    private final ItemRepository itemRepository;
    private final InventarioJogadorRepository inventarioJogadorRepository;

    public ComprarItemHandler(JogadorRepository jogadorRepository,
                              NoJornadaRepository noJornadaRepository,
                              CidadeItemLojaRepository cidadeItemLojaRepository,
                              ItemRepository itemRepository,
                              InventarioJogadorRepository inventarioJogadorRepository) {
        this.jogadorRepository = jogadorRepository;
        this.noJornadaRepository = noJornadaRepository;
        this.cidadeItemLojaRepository = cidadeItemLojaRepository;
        this.itemRepository = itemRepository;
        this.inventarioJogadorRepository = inventarioJogadorRepository;
    }

    @Transactional
    public void handle(ComprarItemCommand command) {
        Jogador jogador = jogadorRepository.findById(command.jogadorId())
                .orElseThrow(() -> new NotFoundException("Jogador nao encontrado."));
        if (!command.cidadeId().equals(jogador.getNoAtualId())) {
            throw new BusinessException("O jogador precisa estar na cidade para comprar.");
        }
        NoJornada cidade = noJornadaRepository.findById(command.cidadeId())
                .orElseThrow(() -> new NotFoundException("Cidade nao encontrada."));
        if (!cidade.ehCidade()) {
            throw new BusinessException("Compras so podem acontecer em cidade.");
        }

        CidadeItemLoja itemLoja = cidadeItemLojaRepository.findByCidadeIdAndItemId(command.cidadeId(), command.itemId())
                .orElseThrow(() -> new NotFoundException("Item nao disponivel na loja desta cidade."));
        Item item = itemRepository.findById(command.itemId())
                .orElseThrow(() -> new NotFoundException("Item nao encontrado."));

        int valorTotal = itemLoja.getPreco() * command.quantidade();
        jogador.debitarMoedas(valorTotal);
        jogadorRepository.save(jogador);

        InventarioJogador inventarioJogador = inventarioJogadorRepository.findByJogadorIdAndItemId(jogador.getId(), item.getId())
                .orElseGet(() -> new InventarioJogador(Ids.unique(), jogador.getId(), item.getId(), 0));
        inventarioJogador.adicionar(command.quantidade());
        inventarioJogadorRepository.save(inventarioJogador);
    }
}
