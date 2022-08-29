package domain;

import java.math.BigDecimal;

public class Evento {
    private TipoEvento tipoEvento;
    private BigDecimal tempo;

    public Evento(TipoEvento tipoEvento, BigDecimal tempo) {
        this.tipoEvento = tipoEvento;
        this.tempo = tempo;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public BigDecimal getTempo() {
        return tempo;
    }
}
