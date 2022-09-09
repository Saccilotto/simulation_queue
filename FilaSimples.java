import domain.Intervalo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FilaSimples implements Fila {
    private Intervalo chegada;
    private Intervalo saida;
    private int servidores;
    private int capacidade;
    private int numeroClientes;
    private int perda = 0;
    private final List<BigDecimal> estados;

    public FilaSimples(Intervalo chegada, Intervalo saida, int servidores, int capacidade) {
        this.chegada = chegada;
        this.saida = saida;
        this.servidores = servidores;
        this.capacidade = capacidade;
        this.estados = new ArrayList<BigDecimal>(capacidade+1);
        for(BigDecimal b:estados) {
            b.add(BigDecimal.ZERO);
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
        return estados.get(this.numeroClientes);
    }

    public void setTempoNoEstadoAtual(BigDecimal tempo) {
        estados.set(this.numeroClientes, tempo);
    }

    public boolean podeRemover() {
        return this.numeroClientes >= servidores;
    }

    public int getNumeroClientes() {
        return this.numeroClientes;
    }

    public List<BigDecimal> getEstados() {
        return estados;
    }
}
