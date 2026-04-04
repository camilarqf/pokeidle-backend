package br.com.pokeidle.importacao.application.command;

import br.com.pokeidle.shared.domain.TipoPokemon;

public record PokemonImportadoDto(Long id,
                                  String nome,
                                  TipoPokemon tipoPrimario,
                                  TipoPokemon tipoSecundario,
                                  int hpBase,
                                  int ataqueBase,
                                  int defesaBase,
                                  int velocidadeBase,
                                  int altura,
                                  int peso,
                                  String spritePrincipal,
                                  int taxaCaptura,
                                  String geracao) {
}
