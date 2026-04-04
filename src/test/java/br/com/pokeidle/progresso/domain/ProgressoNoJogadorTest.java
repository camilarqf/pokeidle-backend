package br.com.pokeidle.progresso.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgressoNoJogadorTest {

    @Test
    void deveConcluirNoAoAtingirMetaDeBatalhas() {
        ProgressoNoJogador progresso = new ProgressoNoJogador("p-1", "j-1", 101L, true);

        progresso.registrarVitorias(5, 5);

        assertTrue(progresso.isConcluido());
    }
}
