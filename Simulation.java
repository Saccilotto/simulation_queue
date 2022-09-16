import domain.Escalonador;
import domain.Evento;
import domain.FilaSimples;
import domain.Intervalo;
import domain.random.Generator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static domain.TipoEvento.CHEGADA;
import static domain.TipoEvento.SAIDA;

public class Simulation {
    private final int MAX_NUMBERS = 100000;
    private BigDecimal globalTime = BigDecimal.ZERO;
    private int count = 0;
    private int numServidores;
    private FilaSimples fila;
    private Intervalo lambda;
    private Intervalo atendimento;
    private int filaTam;

    private Escalonador escalonador;

    private final Generator generator;

    public Simulation(Intervalo lambda, Intervalo atendimento, int numServidores, int filaTam) {
        this.lambda = lambda;
        this.atendimento = atendimento;
        this.filaTam = filaTam;
        this.numServidores = numServidores;
        fila = new FilaSimples(lambda, atendimento, numServidores, filaTam);
        this.escalonador = new Escalonador();
        this.generator = new Generator();
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
        final BigDecimal random = generator.generateRandom()
                .multiply(BigDecimal.valueOf(max + 1 - min))
                .add(BigDecimal.valueOf(min));


        count++;
        return BigDecimal.valueOf(random.doubleValue());
    }

    public void chegada(BigDecimal tempoEvento) {
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        fila.setTempoNoEstadoAtual(fila.getTempoNoEstadoAtual().add(delta));
        globalTime = tempoEvento;
        if (!fila.isFull()) {
            fila.add();
            if (fila.getNumeroClientes() <= fila.getServidores()) {
                //saida(generateRandom(atendimento.getInicio(), atendimento.getFim()).divide(BigDecimal.valueOf(numServidores)));
                escalonador.addEvento(new Evento(SAIDA, tempoEvento.add(generateRandom(atendimento.getInicio(), atendimento.getFim()))));
            }
        } else {
            fila.addPerda();
        }
        escalonador.addEvento(new Evento(CHEGADA, tempoEvento.add(generateRandom(lambda.getInicio(), lambda.getFim()))));
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
            // TODO: remover evento após ser utilizado
            // como acessa sempre o primeiro, pensar numa estrutura que mantem ordem (sort a cada add)
            // e que permite iteração
        }
        System.out.println("Tempo final: " + globalTime);
        System.out.println("Perda: " + fila.getPerda());
        System.out.println("Estados da Fila:");
        for (int i = 0; i < fila.getEstados().length; i++) {
            System.out.println("Prob[" + i + "] = " + fila.getEstados()[i].divide(globalTime, RoundingMode.FLOOR));
        }
    }

}
