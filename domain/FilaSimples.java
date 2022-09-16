package domain;

import domain.Intervalo;

import java.math.BigDecimal;

public class FilaSimples implements Fila {

    private Intervalo chegada;
    private Intervalo saida;

    private int servidores;

    private int capacidade;

    private int numeroClientes;
    private int perda = 0;
    private final BigDecimal[] estados;

    public FilaSimples(Intervalo chegada, Intervalo saida, int servidores, int capacidade) {
        this.chegada = chegada;
        this.saida = saida;
        this.servidores = servidores;
        this.capacidade = capacidade;
        this.estados = new BigDecimal[capacidade+1];
        for (int i = 0; i < capacidade+1; i++) {
            estados[i] = BigDecimal.ZERO;
        }
    }

    public Intervalo getChegada() {
        return chegada;
    }

    public Intervalo getSaida() {
        return saida;
    }

    public int getServidores() {
        return servidores;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public boolean isFull() {
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

    public int getPerda() {
        return perda;
    }

    @Override
    public void addPerda() {
        this.perda++;
    }

    public BigDecimal getTempoNoEstadoAtual() {
        return estados[this.numeroClientes];
    }

    public void setTempoNoEstadoAtual(final BigDecimal tempo) {
        estados[this.numeroClientes] = tempo;
    }

    public boolean podeRemover() {
        return this.numeroClientes >= servidores;
    }

    public int getNumeroClientes() {
        return this.numeroClientes;
    }

    public BigDecimal[] getEstados() {
        return estados;
    }
}
