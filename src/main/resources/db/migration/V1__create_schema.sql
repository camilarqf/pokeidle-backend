create table pokemon_especie (
    id bigint primary key,
    nome varchar(80) not null unique,
    tipo_primario varchar(20) not null,
    tipo_secundario varchar(20),
    hp_base int not null,
    ataque_base int not null,
    defesa_base int not null,
    velocidade_base int not null,
    altura int not null,
    peso int not null,
    sprite_principal varchar(500),
    taxa_captura int not null,
    geracao varchar(60) not null
);

create table pokemon_perfil_design (
    id bigint identity(1,1) primary key,
    pokemon_especie_id bigint not null unique,
    visual_key varchar(60) not null,
    cor_predominante varchar(20) not null,
    constraint fk_pokemon_perfil_design_especie foreign key (pokemon_especie_id) references pokemon_especie(id)
);

create table regiao (
    id bigint primary key,
    nome varchar(80) not null
);

create table no_jornada (
    id bigint primary key,
    regiao_id bigint not null,
    nome varchar(80) not null,
    slug varchar(80) not null unique,
    tipo varchar(20) not null,
    descricao varchar(255) not null,
    ordem_mapa int not null,
    desbloqueado_inicial bit not null,
    permite_batalha_selvagem bit not null,
    cidade_hub bit not null,
    constraint fk_no_jornada_regiao foreign key (regiao_id) references regiao(id)
);

create table no_conexao (
    id bigint identity(1,1) primary key,
    origem_no_id bigint not null,
    destino_no_id bigint not null,
    constraint fk_no_conexao_origem foreign key (origem_no_id) references no_jornada(id),
    constraint fk_no_conexao_destino foreign key (destino_no_id) references no_jornada(id)
);

create table missao_no (
    id bigint identity(1,1) primary key,
    no_jornada_id bigint not null unique,
    tipo_missao varchar(30) not null,
    alvo_quantidade int not null,
    descricao varchar(120) not null,
    constraint fk_missao_no foreign key (no_jornada_id) references no_jornada(id)
);

create table jogador (
    id varchar(36) primary key,
    nome_perfil varchar(80) not null unique,
    saldo_moedas int not null,
    no_atual_id bigint,
    pokemon_inicial_escolhido bit not null,
    criado_em datetime2(6) not null,
    constraint fk_jogador_no_atual foreign key (no_atual_id) references no_jornada(id)
);

create table progresso_no_jogador (
    id varchar(36) primary key,
    jogador_id varchar(36) not null,
    no_jornada_id bigint not null,
    desbloqueado bit not null,
    concluido bit not null,
    batalhas_vencidas int not null,
    atualizado_em datetime2(6) not null,
    constraint fk_progresso_jogador foreign key (jogador_id) references jogador(id),
    constraint fk_progresso_no foreign key (no_jornada_id) references no_jornada(id),
    constraint uk_progresso_jogador_no unique (jogador_id, no_jornada_id)
);

create table pokemon_capturado (
    id varchar(36) primary key,
    jogador_id varchar(36) not null,
    especie_id bigint not null,
    nome varchar(80) not null,
    nivel int not null,
    experiencia int not null,
    hp_atual int not null,
    hp_maximo int not null,
    ataque int not null,
    defesa int not null,
    velocidade int not null,
    tipo_primario varchar(20) not null,
    tipo_secundario varchar(20),
    inicial bit not null,
    ativo bit not null,
    capturado_em datetime2(6) not null,
    constraint fk_pokemon_capturado_jogador foreign key (jogador_id) references jogador(id),
    constraint fk_pokemon_capturado_especie foreign key (especie_id) references pokemon_especie(id)
);

create table item (
    id bigint primary key,
    codigo varchar(30) not null unique,
    nome varchar(80) not null,
    preco_base int not null
);

create table inventario_jogador (
    id varchar(36) primary key,
    jogador_id varchar(36) not null,
    item_id bigint not null,
    quantidade int not null,
    constraint fk_inventario_jogador foreign key (jogador_id) references jogador(id),
    constraint fk_inventario_item foreign key (item_id) references item(id),
    constraint uk_inventario_jogador_item unique (jogador_id, item_id)
);

create table cidade_item_loja (
    id bigint identity(1,1) primary key,
    cidade_id bigint not null,
    item_id bigint not null,
    preco int not null,
    constraint fk_cidade_item_loja_cidade foreign key (cidade_id) references no_jornada(id),
    constraint fk_cidade_item_loja_item foreign key (item_id) references item(id),
    constraint uk_cidade_item unique (cidade_id, item_id)
);

create table batalha (
    id varchar(36) primary key,
    jogador_id varchar(36) not null,
    no_jornada_id bigint not null,
    pokemon_jogador_id varchar(36) not null,
    especie_selvagem_id bigint not null,
    nome_selvagem varchar(80) not null,
    nivel_selvagem int not null,
    hp_atual_selvagem int not null,
    hp_maximo_selvagem int not null,
    ataque_selvagem int not null,
    defesa_selvagem int not null,
    velocidade_selvagem int not null,
    tipo_primario_selvagem varchar(20) not null,
    tipo_secundario_selvagem varchar(20),
    status varchar(20) not null,
    turnos int not null,
    experiencia_concedida int not null,
    criada_em datetime2(6) not null,
    constraint fk_batalha_jogador foreign key (jogador_id) references jogador(id),
    constraint fk_batalha_no foreign key (no_jornada_id) references no_jornada(id),
    constraint fk_batalha_pokemon foreign key (pokemon_jogador_id) references pokemon_capturado(id),
    constraint fk_batalha_especie foreign key (especie_selvagem_id) references pokemon_especie(id)
);

create index idx_pokemon_capturado_jogador on pokemon_capturado(jogador_id);
create index idx_batalha_jogador_status on batalha(jogador_id, status);
create index idx_inventario_jogador on inventario_jogador(jogador_id);
