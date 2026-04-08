alter table jogador add nivel_cap_atual int not null constraint df_jogador_nivel_cap_atual default 12;
GO

alter table progresso_no_jogador add batalhas_selvagens_vencidas int not null constraint df_progresso_selvagens default 0;
alter table progresso_no_jogador add treinadores_derrotados int not null constraint df_progresso_treinadores default 0;
alter table progresso_no_jogador add lider_derrotado bit not null constraint df_progresso_lider default 0;
GO
update progresso_no_jogador set batalhas_selvagens_vencidas = batalhas_vencidas;
GO

create table objetivo_missao_no (
    id bigint identity(1,1) primary key,
    missao_no_id bigint not null,
    tipo_objetivo varchar(40) not null,
    alvo_quantidade int not null,
    descricao varchar(160) not null,
    ordem int not null,
    constraint fk_objetivo_missao_no foreign key (missao_no_id) references missao_no(id)
);
GO

create table time_ativo_slot (
    id bigint identity(1,1) primary key,
    jogador_id varchar(36) not null,
    slot_numero int not null,
    pokemon_capturado_id varchar(36) not null,
    constraint fk_time_ativo_slot_jogador foreign key (jogador_id) references jogador(id),
    constraint fk_time_ativo_slot_pokemon foreign key (pokemon_capturado_id) references pokemon_capturado(id),
    constraint uk_time_ativo_slot unique (jogador_id, slot_numero),
    constraint uk_time_ativo_pokemon unique (pokemon_capturado_id)
);
GO

create table box_pokemon (
    id bigint identity(1,1) primary key,
    jogador_id varchar(36) not null,
    pokemon_capturado_id varchar(36) not null,
    armazenado_em datetime2(6) not null,
    constraint fk_box_pokemon_jogador foreign key (jogador_id) references jogador(id),
    constraint fk_box_pokemon_capturado foreign key (pokemon_capturado_id) references pokemon_capturado(id),
    constraint uk_box_pokemon unique (pokemon_capturado_id)
);
GO

create table treinador_npc (
    id bigint primary key,
    nome varchar(80) not null,
    no_jornada_id bigint not null,
    ginasio_id bigint null,
    lider bit not null,
    recompensa_moedas int not null,
    experiencia_recompensa int not null,
    ordem_desafio int not null,
    constraint fk_treinador_npc_no foreign key (no_jornada_id) references no_jornada(id)
);
GO

create table treinador_npc_pokemon (
    id bigint identity(1,1) primary key,
    treinador_npc_id bigint not null,
    especie_id bigint not null,
    nome varchar(80) not null,
    nivel int not null,
    ordem_equipe int not null,
    constraint fk_treinador_npc_pokemon_treinador foreign key (treinador_npc_id) references treinador_npc(id),
    constraint fk_treinador_npc_pokemon_especie foreign key (especie_id) references pokemon_especie(id)
);
GO

create table ginasio (
    id bigint primary key,
    no_jornada_id bigint not null unique,
    nome varchar(80) not null,
    cidade_id bigint not null,
    lider_treinador_id bigint null,
    badge_codigo varchar(40) not null,
    badge_nome varchar(80) not null,
    constraint fk_ginasio_no foreign key (no_jornada_id) references no_jornada(id),
    constraint fk_ginasio_cidade foreign key (cidade_id) references no_jornada(id)
);
GO

create table jogador_treinador_derrotado (
    id bigint identity(1,1) primary key,
    jogador_id varchar(36) not null,
    treinador_npc_id bigint not null,
    derrotado_em datetime2(6) not null,
    constraint fk_jogador_treinador_jogador foreign key (jogador_id) references jogador(id),
    constraint fk_jogador_treinador_treinador foreign key (treinador_npc_id) references treinador_npc(id),
    constraint uk_jogador_treinador unique (jogador_id, treinador_npc_id)
);
GO

create table badge_jogador (
    id bigint identity(1,1) primary key,
    jogador_id varchar(36) not null,
    codigo varchar(40) not null,
    nome varchar(80) not null,
    obtida_em datetime2(6) not null,
    constraint fk_badge_jogador foreign key (jogador_id) references jogador(id),
    constraint uk_badge_jogador_codigo unique (jogador_id, codigo)
);
GO

create table pokemon_linha_evolutiva (
    id bigint identity(1,1) primary key,
    pokemon_especie_id bigint not null,
    evolui_para_especie_id bigint not null,
    nivel_evolucao int null,
    constraint fk_pokemon_linha_evolutiva_origem foreign key (pokemon_especie_id) references pokemon_especie(id),
    constraint fk_pokemon_linha_evolutiva_destino foreign key (evolui_para_especie_id) references pokemon_especie(id)
);
GO

alter table batalha add tipo varchar(20) not null constraint df_batalha_tipo default 'SELVAGEM';
alter table batalha add treinador_npc_id bigint null;
alter table batalha add nome_oponente varchar(80) null;
alter table batalha add recompensa_moedas int not null constraint df_batalha_moedas default 0;
alter table batalha add captura_permitida bit not null constraint df_batalha_captura default 1;
alter table batalha add indice_oponente_atual int not null constraint df_batalha_indice default 0;
alter table batalha add concluida_primeira_vez bit not null constraint df_batalha_primeira default 0;
alter table batalha add constraint fk_batalha_treinador foreign key (treinador_npc_id) references treinador_npc(id);
GO

create table batalha_oponente_pokemon (
    id bigint identity(1,1) primary key,
    batalha_id varchar(36) not null,
    ordem_equipe int not null,
    especie_id bigint not null,
    nome varchar(80) not null,
    nivel int not null,
    hp_atual int not null,
    hp_maximo int not null,
    ataque int not null,
    defesa int not null,
    velocidade int not null,
    tipo_primario varchar(20) not null,
    tipo_secundario varchar(20) null,
    derrotado bit not null,
    constraint fk_batalha_oponente_pokemon_batalha foreign key (batalha_id) references batalha(id),
    constraint fk_batalha_oponente_pokemon_especie foreign key (especie_id) references pokemon_especie(id),
    constraint uk_batalha_oponente_ordem unique (batalha_id, ordem_equipe)
);
GO

insert into time_ativo_slot (jogador_id, slot_numero, pokemon_capturado_id)
select jogador_id, 1, id
from pokemon_capturado
where ativo = 1
and not exists (
    select 1 from time_ativo_slot slot where slot.pokemon_capturado_id = pokemon_capturado.id
);
GO

insert into box_pokemon (jogador_id, pokemon_capturado_id, armazenado_em)
select jogador_id, id, capturado_em
from pokemon_capturado
where ativo = 0
and not exists (
    select 1 from box_pokemon box where box.pokemon_capturado_id = pokemon_capturado.id
);
GO

create index idx_time_ativo_slot_jogador on time_ativo_slot(jogador_id);
create index idx_box_pokemon_jogador on box_pokemon(jogador_id);
create index idx_treinador_no on treinador_npc(no_jornada_id);
create index idx_badge_jogador_jogador on badge_jogador(jogador_id);
