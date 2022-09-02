package domain;

import domain.random.Generator;

import java.math.BigDecimal;
import java.util.List;

import static domain.TipoEvento.CHEGADA;
import static domain.TipoEvento.SAIDA;
import static domain.TipoEvento.TRANSFERENCIA;

public class FilaTandem implements Fila {

    private final List<FilaSimples> filas;
    private final Generator generator;

    public FilaTandem(List<FilaSimples> filas,
                      Generator generator) {
        this.filas = filas;
        this.generator = generator;
    }

    public List<FilaSimples> getFilas() {
        return filas;
    }

    public void handleChegada(BigDecimal tempoEvento, BigDecimal globalTime, Escalonador escalonador) {
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        setTempoNosEstadosAtuais(delta);
        final FilaSimples primeiraFila = getFirst();

        if (!primeiraFila.isFull()) {
            primeiraFila.add();
            if (primeiraFila.getNumeroClientes() <= primeiraFila.getServidores()) {
                escalonador.addEvento(new EventoRoteamento(TRANSFERENCIA,
                        tempoEvento.add(generator.generateRandom(primeiraFila.getTaxaSaida())),
                        0,
                        1));
            }
        } else {
            primeiraFila.addPerda();
        }
        escalonador.addEvento(new Evento(CHEGADA, tempoEvento.add(generator.generateRandom(primeiraFila.getTaxaChegada()))));
    }

    public void handleSaida(BigDecimal tempoEvento, BigDecimal globalTime, Escalonador escalonador) {
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        setTempoNosEstadosAtuais(delta);
        final FilaSimples ultimaFila = getLast();
        ultimaFila.remove();
        if (ultimaFila.getNumeroClientes() >= ultimaFila.getServidores()) {
            escalonador.addEvento(new Evento(SAIDA, tempoEvento.add(generator.generateRandom(ultimaFila.getTaxaSaida()))));
        }
    }

    private void handleTransferencia(EventoRoteamento evento, BigDecimal globalTime, Escalonador escalonador) {
        final BigDecimal tempoEvento = evento.getTempo();
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        setTempoNosEstadosAtuais(delta);
        final FilaSimples fila1 = filas.get(evento.getIndiceFilaOrigem());
        final FilaSimples fila2 = filas.get(evento.getIndiceFilaFim());

        fila1.remove();
        if (fila1.getNumeroClientes() >= fila1.getServidores()) {
            escalonador.addEvento(new EventoRoteamento(TRANSFERENCIA,
                    tempoEvento.add(generator.generateRandom(fila2.getTaxaSaida())),
                    evento.getIndiceFilaOrigem(),
                    evento.getIndiceFilaFim()));
        }
        if (!fila2.isFull()) {
            fila2.add();
            if (fila2.getNumeroClientes() <= fila2.getServidores()) {
                escalonador.addEvento(getEventBasedOnQueuePosition(fila2, tempoEvento, evento.getIndiceFilaOrigem(), evento.getIndiceFilaFim()));
            }
        } else {
            fila2.addPerda();
        }
    }

    private Evento getEventBasedOnQueuePosition(FilaSimples fila, BigDecimal tempoEvento, int indiceOrigem, int indiceFim) {
        if (indiceFim == filas.size() - 1) {
            return new Evento(SAIDA, tempoEvento.add(generator.generateRandom(fila.getTaxaSaida())));
        }

        return new EventoRoteamento(TRANSFERENCIA,
                tempoEvento.add(generator.generateRandom(fila.getTaxaSaida())),
                indiceOrigem,
                indiceFim);
    }


    private FilaSimples getLast() {
        return filas.get(filas.size() - 1);
    }

    private FilaSimples getFirst() {
        return filas.get(0);
    }

    private void setTempoNosEstadosAtuais(BigDecimal delta) {
        filas.forEach(filaSimples ->
                filaSimples.setTempoNoEstadoAtual(filaSimples.getTempoNoEstadoAtual().add(delta))
        );
    }

    @Override
    public Generator getGenerator() {
        return generator;
    }

    @Override
    public String getPerda() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < filas.size(); i++) {
            builder.append("Fila ").append(i + 1).append("\n")
                    .append(filas.get(i).getPerda())
                    .append("\n");
        }

        return builder.toString();
    }

    @Override
    public void imprimeEstados(BigDecimal tempoFinal) {
        for (int i = 0; i < filas.size(); i++) {
            System.out.println("Fila [" + (i + 1) + "]:");
            filas.get(i).imprimeEstados(tempoFinal);
        }
    }

    @Override
    public BigDecimal handleEvento(Evento evento, BigDecimal globalTime, Escalonador escalonador) {
        final BigDecimal tempoEvento = evento.getTempo();
        if (evento.getTipoEvento().equals(CHEGADA)) {
            this.handleChegada(tempoEvento, globalTime, escalonador);
        } else if (evento.getTipoEvento().equals(SAIDA)) {
            this.handleSaida(tempoEvento, globalTime, escalonador);
        } else {
            this.handleTransferencia((EventoRoteamento) evento, globalTime, escalonador);
        }
        return tempoEvento;
    }
}
