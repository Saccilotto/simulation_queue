package domain;

import java.math.BigDecimal;

public class EventoRoteamento extends Evento {

    public static final int INDICE_EXTERIOR = -1;

    private Integer indiceFilaOrigem;

    private Integer indiceFilaFim;

    public EventoRoteamento(TipoEvento tipoEvento, BigDecimal tempo, int indiceFilaOrigem, int indiceFilaFim) {
        super(tipoEvento, tempo);
        this.indiceFilaOrigem = indiceFilaOrigem;
        this.indiceFilaFim = indiceFilaFim;
    }

    public int getIndiceFilaOrigem() {
        return indiceFilaOrigem;
    }

    public int getIndiceFilaFim() {
        return indiceFilaFim;
    }
}
