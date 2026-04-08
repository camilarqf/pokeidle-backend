insert into pokemon_especie (id, nome, tipo_primario, tipo_secundario, hp_base, ataque_base, defesa_base, velocidade_base, altura, peso, sprite_principal, taxa_captura, geracao)
select base.id, base.nome, base.tipo_primario, base.tipo_secundario, base.hp_base, base.ataque_base, base.defesa_base, base.velocidade_base, base.altura, base.peso, base.sprite_principal, base.taxa_captura, base.geracao
from (
    values
    (1, 'bulbasaur', 'GRASS', 'POISON', 45, 49, 49, 45, 7, 69, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png', 45, 'generation-i'),
    (4, 'charmander', 'FIRE', null, 39, 52, 43, 65, 6, 85, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/4.png', 45, 'generation-i'),
    (7, 'squirtle', 'WATER', null, 44, 48, 65, 43, 5, 90, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/7.png', 45, 'generation-i'),
    (10, 'caterpie', 'BUG', null, 45, 30, 35, 45, 3, 29, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/10.png', 255, 'generation-i'),
    (13, 'weedle', 'BUG', 'POISON', 40, 35, 30, 50, 3, 32, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/13.png', 255, 'generation-i'),
    (16, 'pidgey', 'NORMAL', 'FLYING', 40, 45, 40, 56, 3, 18, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/16.png', 255, 'generation-i'),
    (19, 'rattata', 'NORMAL', null, 30, 56, 35, 72, 3, 35, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/19.png', 255, 'generation-i'),
    (25, 'pikachu', 'ELECTRIC', null, 35, 55, 40, 90, 4, 60, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png', 190, 'generation-i'),
    (27, 'sandshrew', 'GROUND', null, 50, 75, 85, 40, 6, 120, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/27.png', 255, 'generation-i'),
    (74, 'geodude', 'ROCK', 'GROUND', 40, 80, 100, 20, 4, 200, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/74.png', 255, 'generation-i'),
    (95, 'onix', 'ROCK', 'GROUND', 35, 45, 160, 70, 88, 2100, 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/95.png', 45, 'generation-i')
) as base(id, nome, tipo_primario, tipo_secundario, hp_base, ataque_base, defesa_base, velocidade_base, altura, peso, sprite_principal, taxa_captura, geracao)
where not exists (
    select 1
    from pokemon_especie existente
    where existente.id = base.id
);
GO

insert into pokemon_perfil_design (pokemon_especie_id, visual_key, cor_predominante)
select especie.id,
       especie.nome,
       case especie.tipo_primario
           when 'GRASS' then '#78C850'
           when 'FIRE' then '#F08030'
           when 'WATER' then '#6890F0'
           when 'FLYING' then '#A890F0'
           when 'POISON' then '#A040A0'
           when 'ELECTRIC' then '#F8D030'
           when 'BUG' then '#A8B820'
           when 'ROCK' then '#B8A038'
           when 'GROUND' then '#E0C068'
           else '#A8A878'
       end
from pokemon_especie especie
where especie.id in (1, 4, 7, 10, 13, 16, 19, 25, 27, 74, 95)
and not exists (
    select 1
    from pokemon_perfil_design design
    where design.pokemon_especie_id = especie.id
);
GO

update no_jornada
set cidade_hub = 1
where id = 100;

update no_jornada
set tipo = 'GINASIO', descricao = 'Primeiro ginasio oficial de Kanto.', permite_batalha_selvagem = 0, cidade_hub = 0
where id = 105;

if not exists (select 1 from no_jornada where id = 103)
begin
    insert into no_jornada (id, regiao_id, nome, slug, tipo, descricao, ordem_mapa, desbloqueado_inicial, permite_batalha_selvagem, cidade_hub) values
    (103, 1, 'Viridian Forest', 'viridian-forest', 'ROTA', 'Floresta inicial com selvagens e treinadores locais.', 4, 0, 1, 0),
    (104, 1, 'Pewter City', 'pewter-city', 'CIDADE', 'Cidade com loja, cura e acesso ao primeiro ginasio.', 5, 0, 0, 1),
    (105, 1, 'Pewter Gym', 'pewter-gym', 'GINASIO', 'Primeiro ginasio oficial de Kanto.', 6, 0, 0, 0);
end

if not exists (select 1 from no_conexao where origem_no_id = 102 and destino_no_id = 103)
begin
    insert into no_conexao (origem_no_id, destino_no_id) values
    (102, 103),
    (103, 102),
    (103, 104),
    (104, 103),
    (104, 105),
    (105, 104);
end

update missao_no
set tipo_missao = 'OBJETIVOS',
    descricao = 'Complete os objetivos de Route 1.'
where no_jornada_id = 101;

if not exists (select 1 from missao_no where no_jornada_id = 100)
begin
    insert into missao_no (no_jornada_id, tipo_missao, alvo_quantidade, descricao) values
    (100, 'OBJETIVOS', 1, 'Comece a jornada em Pallet Town.'),
    (102, 'OBJETIVOS', 1, 'Chegue a Viridian City.'),
    (103, 'OBJETIVOS', 5, 'Supere os desafios de Viridian Forest.'),
    (104, 'OBJETIVOS', 1, 'Chegue a Pewter City.'),
    (105, 'OBJETIVOS', 3, 'Derrote os treinadores do ginasio e Brock.');
end

delete from objetivo_missao_no;

insert into objetivo_missao_no (missao_no_id, tipo_objetivo, alvo_quantidade, descricao, ordem)
select id, 'ENTRAR_NO', 1, 'Entrar em Pallet Town.', 1 from missao_no where no_jornada_id = 100
union all
select id, 'VENCER_BATALHAS_SELVAGENS', 5, 'Vencer 5 batalhas selvagens.', 1 from missao_no where no_jornada_id = 101
union all
select id, 'ENTRAR_NO', 1, 'Entrar em Viridian City.', 1 from missao_no where no_jornada_id = 102
union all
select id, 'VENCER_BATALHAS_SELVAGENS', 3, 'Vencer 3 batalhas selvagens.', 1 from missao_no where no_jornada_id = 103
union all
select id, 'DERROTAR_TREINADORES', 2, 'Derrotar 2 treinadores locais.', 2 from missao_no where no_jornada_id = 103
union all
select id, 'ENTRAR_NO', 1, 'Entrar em Pewter City.', 1 from missao_no where no_jornada_id = 104
union all
select id, 'DERROTAR_TREINADORES', 2, 'Derrotar os treinadores do ginasio.', 1 from missao_no where no_jornada_id = 105
union all
select id, 'DERROTAR_LIDER', 1, 'Derrotar Brock.', 2 from missao_no where no_jornada_id = 105;

if not exists (select 1 from cidade_item_loja where cidade_id = 104 and item_id = 1)
begin
    insert into cidade_item_loja (cidade_id, item_id, preco) values
    (104, 1, 100),
    (104, 2, 150);
end

if not exists (select 1 from ginasio where id = 301)
begin
    insert into ginasio (id, no_jornada_id, nome, cidade_id, lider_treinador_id, badge_codigo, badge_nome)
    values (301, 105, 'Pewter Gym', 104, null, 'BOULDER_BADGE', 'Boulder Badge');
end

delete from treinador_npc where id in (201, 202, 203, 204, 205);

insert into treinador_npc (id, nome, no_jornada_id, ginasio_id, lider, recompensa_moedas, experiencia_recompensa, ordem_desafio) values
(201, 'Bug Catcher Liam', 103, null, 0, 120, 90, 1),
(202, 'Lass Nina', 103, null, 0, 140, 100, 2),
(203, 'Camper Flint', 105, 301, 0, 180, 140, 1),
(204, 'Junior Trainer Cole', 105, 301, 0, 200, 150, 2),
(205, 'Brock', 105, 301, 1, 500, 260, 3);

insert into treinador_npc_pokemon (treinador_npc_id, especie_id, nome, nivel, ordem_equipe)
values
(201, 10, 'caterpie', 6, 1),
(201, 13, 'weedle', 6, 2),
(202, 16, 'pidgey', 7, 1),
(202, 19, 'rattata', 7, 2),
(203, 74, 'geodude', 10, 1),
(204, 27, 'sandshrew', 11, 1),
(205, 74, 'geodude', 12, 1),
(205, 95, 'onix', 14, 2);
GO

update ginasio
set lider_treinador_id = 205
where id = 301;
