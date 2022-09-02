package domain;

public class Intervalo {
    private final double inicio;
    private final double fim;

    public Intervalo(double inicio, double fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    public double getInicio() {
        return inicio;
    }

    public double getFim() {
        return fim;
    }
}
