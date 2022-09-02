package domain;

import domain.random.Generator;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static domain.TipoEvento.CHEGADA;
import static domain.TipoEvento.SAIDA;

public class FilaRoteamento implements Fila {

    private final List<FilaSimples> filas;

    private final Generator generator;

    private final List<Transicao> transicoes;

    public FilaRoteamento(List<FilaSimples> filas, Generator generator, List<Transicao> transicoes) {
        this.filas = filas;
        this.generator = generator;
        this.transicoes = transicoes;
    }

    public void handleChegada(EventoRoteamento evento, BigDecimal globalTime, Escalonador escalonador) {
        final BigDecimal tempoEvento = evento.getTempo();
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        setTempoNosEstadosAtuais(delta);

        final int indiceFilaAtual = evento.getIndiceFilaFim();
        final FilaSimples filaChegada = filas.get(indiceFilaAtual);

        if (!filaChegada.isFull()) {
            filaChegada.add();
            if (filaChegada.getNumeroClientes() <= filaChegada.getServidores()) {
                if (!generator.canGenerate()) {
                    return;
                }
                Transicao transicao = escolherTransicao(indiceFilaAtual);

                if (!generator.canGenerate()) {
                    return;
                }
                escalonador.addEvento(new EventoRoteamento(transicao.getTipoEvento(),
                        tempoEvento.add(generator.generateRandom(filaChegada.getTaxaSaida())),
                        indiceFilaAtual,
                        transicao.getIndiceDestino()
                ));
            }
        } else {
            filaChegada.addPerda();
        }
        if (!generator.canGenerate()) {
            return;
        }
        escalonador.addEvento(new EventoRoteamento(CHEGADA,
                tempoEvento.add(generator.generateRandom(filaChegada.getTaxaChegada())),
                -1,
                indiceFilaAtual));
    }

    /**
     * Pega transições possiveis e escolhe a possivel
     */
    private Transicao escolherTransicao(int indiceFila) {
        final List<Transicao> transicoesPossiveis = transicoes.stream()
                .filter(transicao -> transicao.getIndiceOrigem().equals(indiceFila) && transicao.getIndiceDestino() != -1)
                .sorted(Comparator.comparingDouble(Transicao::getProbabilidade)) //TODO: CHECAR REVERSE
                .collect(Collectors.toList());

        transicoesPossiveis
                .addAll(transicoes.stream()
                        .filter(transicao -> transicao.getIndiceDestino() == -1 && transicao.getIndiceOrigem().equals(indiceFila))
                        .collect(Collectors.toList()));

        return escolherComBaseNaProbabilidade(transicoesPossiveis);
    }

    private Transicao escolherComBaseNaProbabilidade(List<Transicao> transicoesPossiveis) {
        if (transicoesPossiveis.size() == 1) {
            return transicoesPossiveis.get(0);
        }

        final double probabilidadeMaior = transicoesPossiveis.get(0).getProbabilidade();

        Map<Integer, Double> filasProbabilidade = new HashMap<>();
        filasProbabilidade.put(0, probabilidadeMaior);
        double probabildadeCumulativa = probabilidadeMaior;
        for (int i = 1; i < transicoesPossiveis.size(); i++) {
            probabildadeCumulativa += transicoesPossiveis.get(i).getProbabilidade();
            filasProbabilidade.put(i, probabildadeCumulativa);
        }

        final List<Map.Entry<Integer, Double>> listaFilaProbabilidadeCumulativa = filasProbabilidade.entrySet().stream()
                .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                .collect(Collectors.toList());

        final double random = generator.generateRandom().doubleValue();
        for (final Map.Entry<Integer, Double> atual : listaFilaProbabilidadeCumulativa) {
            if (random <= atual.getValue()) {
                return transicoesPossiveis.get(atual.getKey());
            }
        }
        return transicoesPossiveis.get(transicoesPossiveis.size() - 1);
    }

    public void handleSaida(EventoRoteamento evento, BigDecimal globalTime, Escalonador escalonador) {
        final BigDecimal tempoEvento = evento.getTempo();
        final BigDecimal delta = tempoEvento.subtract(globalTime);
        setTempoNosEstadosAtuais(delta);
        final int indiceFilaAtual = evento.getIndiceFilaOrigem();
        final FilaSimples filaSaida = filas.get(indiceFilaAtual);

        filaSaida.remove();
        if (filaSaida.getNumeroClientes() >= filaSaida.getServidores()) {
            final Transicao transicao = escolherTransicao(indiceFilaAtual);
            if (!generator.canGenerate()) {
                return;
            }
            escalonador.addEvento(new EventoRoteamento(
                    SAIDA,
                    tempoEvento.add(generator.generateRandom(filaSaida.getTaxaSaida())),
                    indiceFilaAtual,
                    transicao.getIndiceDestino()
            ));
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
            if (!generator.canGenerate()) {
                return;
            }
            final Transicao transicao = escolherTransicao(evento.getIndiceFilaOrigem());
            if (!generator.canGenerate()) {
                return;
            }
            escalonador.addEvento(new EventoRoteamento(
                    transicao.getTipoEvento(),
                    tempoEvento.add(generator.generateRandom(fila1.getTaxaSaida())),
                    transicao.getIndiceOrigem(),
                    transicao.getIndiceDestino()));
        }
        if (!fila2.isFull()) {
            fila2.add();
            if (fila2.getNumeroClientes() <= fila2.getServidores()) {
                if (!generator.canGenerate()) {
                    return;
                }
                final Transicao transicao = escolherTransicao(evento.getIndiceFilaFim());
                if (!generator.canGenerate()) {
                    return;
                }
                escalonador.addEvento(new EventoRoteamento(
                        transicao.getTipoEvento(),
                        tempoEvento.add(generator.generateRandom(fila2.getTaxaSaida())),
                        transicao.getIndiceOrigem(),
                        transicao.getIndiceDestino()));
            }
        } else {
            fila2.addPerda();
        }
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
        if (evento instanceof EventoRoteamento) {
            EventoRoteamento eventoRoteamento = (EventoRoteamento) evento;

            if (evento.getTipoEvento().equals(CHEGADA)) {
                this.handleChegada(eventoRoteamento, globalTime, escalonador);
            } else if (evento.getTipoEvento().equals(SAIDA)) {
                this.handleSaida(eventoRoteamento, globalTime, escalonador);
            } else {
                this.handleTransferencia(eventoRoteamento, globalTime, escalonador);
            }
        }
        return tempoEvento;
    }


}
