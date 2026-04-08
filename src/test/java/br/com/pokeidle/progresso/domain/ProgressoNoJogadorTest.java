package br.com.pokeidle.progresso.domain;

import br.com.pokeidle.mundo.domain.ObjetivoMissaoNo;
import br.com.pokeidle.mundo.domain.TipoObjetivoMissaoNo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgressoNoJogadorTest {

    @Test
    void deveConcluirNoAoAtingirMetaDeBatalhas() {
        ProgressoNoJogador progresso = new ProgressoNoJogador("p-1", "j-1", 101L, true);

        progresso.registrarVitorias(5, 5);

        assertTrue(progresso.isConcluido());
    }

    @Test
    void deveConcluirMissaoComObjetivosDeTreinadorELider() {
        ProgressoNoJogador progresso = new ProgressoNoJogador("p-2", "j-1", 105L, true);
        List<ObjetivoMissaoNo> objetivos = List.of(
                new ObjetivoMissaoNo(1L, TipoObjetivoMissaoNo.DERROTAR_TREINADORES, 2, "Derrotar treinadores", 1),
                new ObjetivoMissaoNo(1L, TipoObjetivoMissaoNo.DERROTAR_LIDER, 1, "Derrotar lider", 2)
        );

        progresso.registrarTreinadorDerrotado(objetivos);
        assertFalse(progresso.isConcluido());

        progresso.registrarTreinadorDerrotado(objetivos);
        progresso.registrarLiderDerrotado(objetivos);

        assertTrue(progresso.isConcluido());
    }
}
