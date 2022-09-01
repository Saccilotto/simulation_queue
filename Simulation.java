import domain.Escalonador;
import domain.Evento;
import domain.Intervalo;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static domain.TipoEvento.CHEGADA;
import static domain.TipoEvento.SAIDA;

public class Simulation {
    private final int MAX_NUMBERS = 100000;
    private BigDecimal globalTime = BigDecimal.ZERO;
    private static int count = 0;
    private int numServidores;
    private FilaSimples fila;
    private Intervalo lambda;
    private Intervalo atendimento;
    private int filaTam;

    private Escalonador escalonador;

    public Simulation(Intervalo lambda, Intervalo atendimento, int numServidores, int filaTam) {
        this.lambda = lambda;
        this.atendimento = atendimento;
        this.filaTam = filaTam;
        this.numServidores = numServidores;
        fila = new FilaSimples(lambda, atendimento, numServidores, filaTam);
        simulate();
    }

    public Intervalo getLambda() {
        return lambda;
    }

    public Intervalo getAtendimento() {
        return atendimento;
    }

    public int getServidores() {
        return numServidores;
    }

    public int getFilaTam() {
        return filaTam;
    }

    public BigDecimal generateRandom(int min, int max) {
        Random rand = new Random(System.currentTimeMillis());
        double n = rand.nextDouble() * (max - min) + min;

        count++;
        return BigDecimal.valueOf(n);
    }

    public void chegada(BigDecimal tempoEvento) {
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        fila.setTempoNoEstadoAtual(fila.getTempoNoEstadoAtual().add(delta));
        globalTime = tempoEvento;
        if (!fila.isFull()) {
            fila.add();
            if (fila.getNumeroClientes() <= fila.getServidores()) {
                //saida(generateRandom(atendimento.getInicio(), atendimento.getFim()).divide(BigDecimal.valueOf(numServidores)));
                escalonador.addEvento(new Evento(SAIDA, tempoEvento));
            }
        }
        else {
            fila.addPerda();
        }
        escalonador.addEvento(new Evento(CHEGADA, tempoEvento));
    }

    private void registraEvento(Evento evento) {
        System.out.println("Evento de " + evento.getTipoEvento() + " no tempo " + evento.getTempo());
    }

    public void saida(BigDecimal tempoEvento) {
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        fila.setTempoNoEstadoAtual(fila.getTempoNoEstadoAtual().add(delta));
        globalTime = tempoEvento;
            fila.remove();
            if (fila.podeRemover()) {
                // saida(generateRandom(atendimento.getInicio(), atendimento.getFim()).divide(BigDecimal.valueOf(numServidores)));
                escalonador.addEvento(new Evento(SAIDA, tempoEvento));
            }
    }


    public void simulate() {
        escalonador.addEvento(new Evento(CHEGADA, BigDecimal.valueOf(3)));

        int i = 0;
        while (i < escalonador.getNumeroEventos()) {

            final Evento evento = escalonador.getEvento(i);

            if (evento.getTipoEvento().equals(CHEGADA)) {
                chegada(globalTime.add(generateRandom(minimo, maximo)));
                return;
            }
            saida(globalTime.add(generateRandom(minimo, maximo)));
        }
    }

}
