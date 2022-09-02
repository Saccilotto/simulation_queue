package domain;

import domain.random.Generator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static domain.TipoEvento.CHEGADA;
import static domain.TipoEvento.SAIDA;

public class FilaSimples implements Fila {

    private Intervalo taxaChegada;
    private Intervalo taxaSaida;

    private int servidores;

    private int capacidade;

    private final Generator generator;

    private int numeroClientes;
    private int perda;
    private final List<BigDecimal> estados;

    public FilaSimples(Intervalo taxaChegada, Intervalo taxaSaida, int servidores, int capacidade, Generator generator) {
        this.taxaChegada = taxaChegada;
        this.taxaSaida = taxaSaida;
        this.servidores = servidores;
        this.capacidade = capacidade;
        this.estados = new ArrayList();
        this.generator = generator;
    }

    public Intervalo getTaxaChegada() {
        return taxaChegada;
    }

    public Intervalo getTaxaSaida() {
        return taxaSaida;
    }

    public int getServidores() {
        return servidores;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public boolean isFull() {
        if (capacidade == -1) {
            return false;
        }
        return numeroClientes == capacidade;
    }

    public void add() {
        numeroClientes++;
    }

    public boolean isEmpty() {
        return numeroClientes == 0;
    }

    public void remove() {
        numeroClientes--;
    }

    public String getPerda() {
        return "Perda: " + perda;
    }

    public void addPerda() {
        this.perda++;
    }

    public void handleChegada(BigDecimal tempoEvento, BigDecimal globalTime, Escalonador escalonador) {
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        this.setTempoNoEstadoAtual(this.getTempoNoEstadoAtual().add(delta));
        if (!this.isFull()) {
            this.add();
            if (this.getNumeroClientes() <= this.getServidores()) {
                escalonador.addEvento(new Evento(SAIDA, tempoEvento.add(generator.generateRandom(taxaSaida.getInicio(), taxaSaida.getFim()))));
            }
        } else {
            this.addPerda();
        }
        escalonador.addEvento(new Evento(CHEGADA, tempoEvento.add(generator.generateRandom(taxaChegada.getInicio(), taxaChegada.getFim()))));
    }

    public void handleSaida(BigDecimal tempoEvento, BigDecimal globalTime, Escalonador escalonador) {
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        this.setTempoNoEstadoAtual(this.getTempoNoEstadoAtual().add(delta));
        this.remove();
        if (this.podeRemover()) {
            escalonador.addEvento(new Evento(SAIDA, tempoEvento.add(generator.generateRandom(taxaSaida.getInicio(), taxaSaida.getFim()))));
        }
    }

    public BigDecimal getTempoNoEstadoAtual() {
        return estados.size() > numeroClientes ? estados.get(numeroClientes) : BigDecimal.ZERO;
    }

    public void setTempoNoEstadoAtual(final BigDecimal tempo) {
        if (estados.size() > numeroClientes) {
            estados.set(numeroClientes, tempo);
            return;
        }
        estados.add(numeroClientes, tempo);
    }

    public boolean podeRemover() {
        return this.numeroClientes >= servidores;
    }

    public int getNumeroClientes() {
        return this.numeroClientes;
    }

    @Override
    public Generator getGenerator() {
        return generator;
    }

    @Override
    public void imprimeEstados(BigDecimal tempoFinal) {
        System.out.println("Estados da Fila:");
        for (int i = 0; i < estados.size(); i++) {
            System.out.println("Prob[" + i + "] = " + estados.get(i).divide(tempoFinal, RoundingMode.FLOOR));
        }
    }

    @Override
    public BigDecimal handleEvento(Evento evento, BigDecimal globalTime, Escalonador escalonador) {
        final BigDecimal tempoEvento = evento.getTempo();
        if (evento.getTipoEvento().equals(CHEGADA)) {
            this.handleChegada(tempoEvento, globalTime, escalonador);
        } else {
            this.handleSaida(tempoEvento, globalTime, escalonador);
        }
        return tempoEvento;
    }
}
