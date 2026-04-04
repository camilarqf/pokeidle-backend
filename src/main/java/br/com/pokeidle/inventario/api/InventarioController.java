package br.com.pokeidle.inventario.api;

import br.com.pokeidle.inventario.application.command.ComprarItemCommand;
import br.com.pokeidle.inventario.application.command.ComprarItemHandler;
import br.com.pokeidle.inventario.application.command.CurarTimeCommand;
import br.com.pokeidle.inventario.application.command.CurarTimeHandler;
import br.com.pokeidle.inventario.application.query.InventarioItemDto;
import br.com.pokeidle.inventario.application.query.LojaItemDto;
import br.com.pokeidle.inventario.application.query.ObterInventarioHandler;
import br.com.pokeidle.inventario.application.query.ObterInventarioQuery;
import br.com.pokeidle.inventario.application.query.ObterLojaDaCidadeHandler;
import br.com.pokeidle.inventario.application.query.ObterLojaDaCidadeQuery;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
public class InventarioController {

    private final ObterInventarioHandler obterInventarioHandler;
    private final ObterLojaDaCidadeHandler obterLojaDaCidadeHandler;
    private final ComprarItemHandler comprarItemHandler;
    private final CurarTimeHandler curarTimeHandler;

    public InventarioController(ObterInventarioHandler obterInventarioHandler,
                                ObterLojaDaCidadeHandler obterLojaDaCidadeHandler,
                                ComprarItemHandler comprarItemHandler,
                                CurarTimeHandler curarTimeHandler) {
        this.obterInventarioHandler = obterInventarioHandler;
        this.obterLojaDaCidadeHandler = obterLojaDaCidadeHandler;
        this.comprarItemHandler = comprarItemHandler;
        this.curarTimeHandler = curarTimeHandler;
    }

    @GetMapping("/jogadores/{id}/inventario")
    public List<InventarioItemDto> obterInventario(@PathVariable String id) {
        return obterInventarioHandler.handle(new ObterInventarioQuery(id));
    }

    @GetMapping("/cidades/{cidadeId}/loja")
    public List<LojaItemDto> obterLoja(@PathVariable Long cidadeId) {
        return obterLojaDaCidadeHandler.handle(new ObterLojaDaCidadeQuery(cidadeId));
    }

    @PostMapping("/jogadores/{id}/compras")
    public ResponseEntity<Void> comprar(@PathVariable String id, @RequestBody CompraRequest request) {
        comprarItemHandler.handle(new ComprarItemCommand(id, request.cidadeId(), request.itemId(), request.quantidade()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/jogadores/{id}/curar")
    public ResponseEntity<Void> curar(@PathVariable String id) {
        curarTimeHandler.handle(new CurarTimeCommand(id));
        return ResponseEntity.ok().build();
    }

    public record CompraRequest(@NotNull Long cidadeId, @NotNull Long itemId, @Min(1) int quantidade) {
    }
}
