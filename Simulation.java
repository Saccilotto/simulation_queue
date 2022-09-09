import domain.Escalonador;
import domain.Evento;
import domain.Intervalo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static domain.TipoEvento.CHEGADA;
import static domain.TipoEvento.TRANSICAO;
import static domain.TipoEvento.SAIDA;

public class Simulation {
    private BigDecimal globalTime = BigDecimal.ZERO;
    private int count = 0;
    private FilaTandem fila;
    private Intervalo chegada;
    private Intervalo atendimento;
    private Escalonador escalonador;

    public Simulation(Intervalo chegada, Intervalo atendimento, FilaTandem fila) {
        this.chegada = chegada;
        this.atendimento = atendimento;
        this.escalonador = new Escalonador();
        this.fila = fila;
    }

    public Intervalo getchegada() {
        return chegada;
    }

    public Intervalo getAtendimento() {
        return atendimento;
    }

    public BigDecimal generateRandom(int min, int max) {
        Random rand = new Random(System.currentTimeMillis());
        double n = rand.nextDouble() * (max - min) + min;

        count++;
        return BigDecimal.valueOf(n);
    }

    public void chegada(BigDecimal tempoEvento) {
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        fila.getFilaTandem().get(0).setTempoNoEstadoAtual(fila.getFilaTandem().get(0).getTempoNoEstadoAtual().add(delta));
        globalTime = tempoEvento;
        if(!fila.getFilaTandem().get(0).isFull()) {
            fila.getFilaTandem().get(0).add();
            if (fila.getFilaTandem().get(0).getNumeroClientes() <= fila.getFilaTandem().get(0).getServidores()) {
                escalonador.addEvento(new Evento(TRANSICAO, tempoEvento.add(generateRandom(atendimento.getInicio(), atendimento.getFim())), 0, 1));
            }
        } else {
            fila.getFilaTandem().get(0).addPerda();
        }
        escalonador.addEvento(new Evento(CHEGADA, tempoEvento.add(generateRandom(chegada.getInicio(), chegada.getFim()))));
    }

    public void transicao(BigDecimal tempoEvento, int origem) {
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        fila.getFilaTandem().get(origem++).setTempoNoEstadoAtual(fila.getFilaTandem().get(0).getTempoNoEstadoAtual().add(delta));
        globalTime = tempoEvento;
        fila.getFilaTandem().get(origem).remove();
        int aux = origem + 2;
        if(fila.getFilaTandem().get(origem).getNumeroClientes() >= fila.getFilaTandem().get(origem).getServidores()) {
            escalonador.addEvento(new Evento(TRANSICAO, tempoEvento.add(generateRandom(atendimento.getInicio(), atendimento.getFim())), origem++, aux));
        }

        if( fila.getFilaTandem().get(aux)) {
            
        }
        

    }

    public void saida(BigDecimal tempoEvento) {
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        fila.getFilaTandem().get(fila.getFilaTandem().size()).setTempoNoEstadoAtual(fila.getFilaTandem().get(fila.getFilaTandem().size()).getTempoNoEstadoAtual().add(delta));
        globalTime = tempoEvento;
        fila.getFilaTandem().get(fila.getFilaTandem().size()).remove();
        if (fila.getFilaTandem().get(fila.getFilaTandem().size()).podeRemover()) {
            escalonador.addEvento(new Evento(SAIDA, tempoEvento.add(generateRandom(atendimento.getInicio(), atendimento.getFim()))));
        }
    }

    public void simulate() {
        escalonador.addEvento(new Evento(CHEGADA, BigDecimal.valueOf(3)));

        while (count < 100000) {
            final Evento evento = escalonador.getEvento(0);

            if (evento.getTipoEvento().equals(CHEGADA)) {
                chegada(evento.getTempo());
            } else {
                saida(evento.getTempo());
            }
            escalonador.remove(0);
        }
        System.out.println("Tempo final: " + globalTime);
        System.out.println("Perda: " + fila.getPerda());
        System.out.println("Estados da Fila:");
        for (int i = 0; i < fila.getEstados().size(); i++) {
            System.out.println("Prob[" + i + "] = " + fila.getEstados().get(i).divide(globalTime, RoundingMode.FLOOR));
        }
    }

}
