insert into regiao (id, nome) values (1, 'Kanto');

insert into no_jornada (id, regiao_id, nome, slug, tipo, descricao, ordem_mapa, desbloqueado_inicial, permite_batalha_selvagem, cidade_hub) values
(100, 1, 'Pallet Town', 'pallet-town', 'CIDADE', 'Cidade inicial tranquila para comecar a jornada.', 1, 1, 0, 0),
(101, 1, 'Route 1', 'route-1', 'ROTA', 'Primeira rota com encontros simples e missao de progresso.', 2, 1, 1, 0),
(102, 1, 'Viridian City', 'viridian-city', 'CIDADE', 'Hub urbano inicial com cura e loja.', 3, 0, 0, 1);

insert into no_conexao (origem_no_id, destino_no_id) values
(100, 101),
(101, 100),
(101, 102),
(102, 101);

insert into missao_no (no_jornada_id, tipo_missao, alvo_quantidade, descricao) values
(101, 'VENCER_BATALHAS', 5, 'Vencer 5 batalhas selvagens para liberar Viridian City.');

insert into item (id, codigo, nome, preco_base) values
(1, 'POKE_BALL', 'Poke Ball', 100),
(2, 'POTION', 'Potion', 150);

insert into cidade_item_loja (cidade_id, item_id, preco) values
(102, 1, 100),
(102, 2, 150);
