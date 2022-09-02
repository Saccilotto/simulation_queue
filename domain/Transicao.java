package domain;

public class Transicao {

    private final Integer indiceOrigem;

    private final Integer indiceDestino;

    private final double probabilidade;

    private final TipoEvento tipoEvento;

    public Transicao(Integer indiceOrigem, Integer indiceDestino, double probabilidade, TipoEvento tipoEvento) {
        this.indiceOrigem = indiceOrigem;
        this.indiceDestino = indiceDestino;
        this.probabilidade = probabilidade;
        this.tipoEvento = tipoEvento;
    }


    public Integer getIndiceOrigem() {
        return indiceOrigem;
    }

    public Integer getIndiceDestino() {
        return indiceDestino;
    }

    public double getProbabilidade() {
        return probabilidade;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }
}
