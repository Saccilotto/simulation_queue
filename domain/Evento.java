package domain;

import java.math.BigDecimal;

public class Evento {
    private TipoEvento tipoEvento;
    private BigDecimal tempo;
    private int destino;
 
    public Evento(TipoEvento tipoEvento, BigDecimal tempo) {
        this.tipoEvento = tipoEvento;
        this.tempo = tempo;
    }

    public Evento(TipoEvento tipoEvento, BigDecimal tempo, int destino) {
        this.tipoEvento = tipoEvento;
        this.tempo = tempo;
        this.destino = destino;
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
