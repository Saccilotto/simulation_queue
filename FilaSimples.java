import domain.Intervalo;

import java.math.BigInteger;
import java.util.LinkedList;

public class FilaSimples implements Fila {

    private Intervalo chegada;
    private Intervalo saida;

    private int servidores;

    private int capacidade;

    private int capacidadeAtual;


    public FilaSimples(Intervalo chegada, Intervalo saida, int servidores, int capacidade) {
        this.chegada = chegada;
        this.saida = saida;
        this.servidores = servidores;
        this.capacidade = capacidade;
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
        return capacidadeAtual == capacidade;
    }

    public void add() {
        capacidadeAtual++;
    }

    public boolean isEmpty() {
        return capacidadeAtual == 0;
    }

    public void remove() {
        capacidadeAtual--;
    }
}
