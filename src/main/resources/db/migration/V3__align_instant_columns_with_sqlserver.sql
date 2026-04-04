if exists (select 1 from sys.columns where object_id = object_id('jogador') and name = 'criado_em' and system_type_id = 189)
begin
    exec('alter table jogador add criado_em_tmp datetime2(6) null');
    exec('update jogador set criado_em_tmp = sysutcdatetime()');
    exec('alter table jogador drop column criado_em');
    exec sp_rename 'jogador.criado_em_tmp', 'criado_em', 'column';
end
alter table jogador alter column criado_em datetime2(6) not null;

if exists (select 1 from sys.columns where object_id = object_id('progresso_no_jogador') and name = 'atualizado_em' and system_type_id = 189)
begin
    exec('alter table progresso_no_jogador add atualizado_em_tmp datetime2(6) null');
    exec('update progresso_no_jogador set atualizado_em_tmp = sysutcdatetime()');
    exec('alter table progresso_no_jogador drop column atualizado_em');
    exec sp_rename 'progresso_no_jogador.atualizado_em_tmp', 'atualizado_em', 'column';
end
alter table progresso_no_jogador alter column atualizado_em datetime2(6) not null;

if exists (select 1 from sys.columns where object_id = object_id('pokemon_capturado') and name = 'capturado_em' and system_type_id = 189)
begin
    exec('alter table pokemon_capturado add capturado_em_tmp datetime2(6) null');
    exec('update pokemon_capturado set capturado_em_tmp = sysutcdatetime()');
    exec('alter table pokemon_capturado drop column capturado_em');
    exec sp_rename 'pokemon_capturado.capturado_em_tmp', 'capturado_em', 'column';
end
alter table pokemon_capturado alter column capturado_em datetime2(6) not null;

if exists (select 1 from sys.columns where object_id = object_id('batalha') and name = 'criada_em' and system_type_id = 189)
begin
    exec('alter table batalha add criada_em_tmp datetime2(6) null');
    exec('update batalha set criada_em_tmp = sysutcdatetime()');
    exec('alter table batalha drop column criada_em');
    exec sp_rename 'batalha.criada_em_tmp', 'criada_em', 'column';
end
alter table batalha alter column criada_em datetime2(6) not null;
