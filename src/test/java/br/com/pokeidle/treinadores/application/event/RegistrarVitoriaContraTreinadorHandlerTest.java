package br.com.pokeidle.treinadores.application.event;

import br.com.pokeidle.jogador.domain.BadgeJogadorRepository;
import br.com.pokeidle.jogador.domain.CodigoBadge;
import br.com.pokeidle.jogador.domain.Jogador;
import br.com.pokeidle.jogador.domain.JogadorRepository;
import br.com.pokeidle.progresso.application.command.AtualizarProgressoDoNoHandler;
import br.com.pokeidle.shared.application.DomainEventPublisher;
import br.com.pokeidle.treinadores.domain.Ginasio;
import br.com.pokeidle.treinadores.domain.GinasioRepository;
import br.com.pokeidle.treinadores.domain.JogadorTreinadorDerrotadoRepository;
import br.com.pokeidle.treinadores.domain.LiderDeGinasioDerrotadoDomainEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegistrarVitoriaContraTreinadorHandlerTest {

    @Test
    void deveConcederBadgeEAtualizarLevelCapAoDerrotarBrock() throws Exception {
        JogadorTreinadorDerrotadoRepository derrotadoRepository = mock(JogadorTreinadorDerrotadoRepository.class);
        JogadorRepository jogadorRepository = mock(JogadorRepository.class);
        BadgeJogadorRepository badgeJogadorRepository = mock(BadgeJogadorRepository.class);
        GinasioRepository ginasioRepository = mock(GinasioRepository.class);
        AtualizarProgressoDoNoHandler atualizarProgressoDoNoHandler = mock(AtualizarProgressoDoNoHandler.class);
        DomainEventPublisher domainEventPublisher = mock(DomainEventPublisher.class);
        RegistrarVitoriaContraTreinadorHandler handler = new RegistrarVitoriaContraTreinadorHandler(
                derrotadoRepository,
                jogadorRepository,
                badgeJogadorRepository,
                ginasioRepository,
                atualizarProgressoDoNoHandler,
                domainEventPublisher
        );

        Jogador jogador = Jogador.criar("j-1", "Red", 100L);
        Ginasio ginasio = ginasio("BOULDER_BADGE", "Boulder Badge");

        when(derrotadoRepository.findByJogadorIdAndTreinadorNpcId("j-1", 205L)).thenReturn(Optional.empty());
        when(jogadorRepository.findById("j-1")).thenReturn(Optional.of(jogador));
        when(ginasioRepository.findByNoJornadaId(105L)).thenReturn(Optional.of(ginasio));
        when(badgeJogadorRepository.findByJogadorIdAndCodigo("j-1", CodigoBadge.BOULDER_BADGE)).thenReturn(Optional.empty());

        handler.on(new LiderDeGinasioDerrotadoDomainEvent("j-1", 205L, 301L, 105L, 500, 260, "pk-1"));

        ArgumentCaptor<Jogador> jogadorCaptor = ArgumentCaptor.forClass(Jogador.class);
        verify(jogadorRepository).save(jogadorCaptor.capture());
        assertEquals(18, jogadorCaptor.getValue().getNivelCapAtual());
        verify(badgeJogadorRepository).save(any());
        verify(domainEventPublisher, atLeast(2)).publishAll(any(List.class));
    }

    private Ginasio ginasio(String codigo, String nome) throws Exception {
        var constructor = Ginasio.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Ginasio ginasio = constructor.newInstance();
        setField(ginasio, "id", 301L);
        setField(ginasio, "noJornadaId", 105L);
        setField(ginasio, "nome", "Pewter Gym");
        setField(ginasio, "cidadeId", 104L);
        setField(ginasio, "badgeCodigo", codigo);
        setField(ginasio, "badgeNome", nome);
        setField(ginasio, "liderTreinadorId", 205L);
        return ginasio;
    }

    private void setField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
