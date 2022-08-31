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

    public void chegada(BigDecimal tempo) {
        globalTime = globalTime.add(tempo);
        // contabilizar o tempo no array de tempos da fila
        // delta = tempodoevento - tempoglobal
        // tempofila[fila.clientes()] = tempofila[fila.clientes()] + delta
        // globalTime = tempodoevento
        if (!fila.isFull()) {
            fila.add();
            if (usuario que estao na fila forem menor ou igual ao numero de servidores da fila) {
                saida(generateRandom(atendimento.getInicio(), atendimento.getFim()).divide(BigDecimal.valueOf(numServidores)));
            }
            //registraEvento(new Evento(CHEGADA, globalTime));
        }
        else {
            //perda++
        }
        //agenda chegada

    }

    private void registraEvento(Evento evento) {
        System.out.println("Evento de " + evento.getTipoEvento() + " no tempo " + evento.getTempo());
    }

    public void saida(BigDecimal tempo) {
        globalTime = globalTime.add(tempo);
   // contabilizar o tempo no array de tempos da fila
        // delta = tempodoevento - tempoglobal
        // tempofila[fila.clientes()] = tempofila[fila.clientes()] + delta
        // globalTime = tempodoevento        //if (!fila.isEmpty()) {
            fila.remove();
            if (usuario que estao na fila forem maior ou igual ao numero de servidores da fila) {
                saida(generateRandom(atendimento.getInicio(), atendimento.getFim()).divide(BigDecimal.valueOf(numServidores)));
            }
            //registraEvento(new Evento(SAIDA, globalTime));
        //}
    }


    public void simulate() {
        Intervalo aux = new Intervalo(1,5);
        chegada(BigDecimal.valueOf(3));
        while (count < MAX_NUMBERS) {
            chegada(generateRandom(lambda.getInicio(), lambda.getFim()));
        }
    }
    
}
