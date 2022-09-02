import domain.Escalonador;
import domain.Evento;
import domain.Fila;
import domain.FilaTandem;
import domain.Intervalo;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static domain.TipoEvento.CHEGADA;
import static domain.TipoEvento.SAIDA;

public class Simulation {
    private BigDecimal globalTime = BigDecimal.ZERO;
    private int count = 0;
    private Fila fila;
    private Intervalo lambda;
    private Intervalo atendimento;

    private Escalonador escalonador;

    public Simulation(Fila fila) {
        this.fila = fila;
        this.escalonador = new Escalonador();
    }

    public void simulate(Evento primeiroEvento) {
        escalonador.addEvento(primeiroEvento);

        while (fila.getGenerator().canGenerate()) {

            final Evento evento = escalonador.getEvento(0);

            handleEvento(evento, globalTime, escalonador);
            escalonador.remove(0);
        }
        System.out.println("Tempo final: " + globalTime);
        System.out.println(fila.getPerda());
        fila.imprimeEstados(globalTime);
    }

    private void handleEvento(Evento evento, BigDecimal globalTime, Escalonador escalonador) {
        this.globalTime = fila.handleEvento(evento, globalTime, escalonador);
    }
}
