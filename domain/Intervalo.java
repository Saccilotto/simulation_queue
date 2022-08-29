package domain;

public class Intervalo {
    private final int inicio;
    private final int fim;

    public Intervalo(int inicio, int fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    public int getInicio() {
        return inicio;
    }

    public int getFim() {
        return fim;
    }
}
