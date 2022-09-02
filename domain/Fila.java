package domain;

import domain.random.Generator;

import java.math.BigDecimal;

public interface Fila {


    Generator getGenerator();

    String getPerda();

    void imprimeEstados(BigDecimal tempoFinal);

    BigDecimal handleEvento(Evento evento, BigDecimal globalTime, Escalonador escalonador);
}
