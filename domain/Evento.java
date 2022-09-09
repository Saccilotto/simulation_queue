package domain;

import java.math.BigDecimal;

public class Evento {
    private TipoEvento tipoEvento;
    private BigDecimal tempo;
    private int origem;
    private int destino;
 
    public Evento(TipoEvento tipoEvento, BigDecimal tempo) {
        this.tipoEvento = tipoEvento;
        this.tempo = tempo;
    }

    public Evento(TipoEvento tipoEvento, BigDecimal tempo, int origem, int destino) {
        this.tipoEvento = tipoEvento;
        this.tempo = tempo;
        this.origem = origem;
        this.destino = destino;
    }

    public int getOrigem() {
        return origem;
    }

    public int getDestino() {
        return destino;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public BigDecimal getTempo() {
        return tempo;
    }
}
