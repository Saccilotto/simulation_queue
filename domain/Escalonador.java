package domain;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Escalonador {
    private List<Evento> eventos;

    public Escalonador() {
        this.eventos = new LinkedList<>();
    }

    public void addEvento(Evento evento) {
        eventos.add(evento);

        eventos = eventos.stream().sorted(Comparator.comparing(Evento::getTempo)).collect(Collectors.toList());
    }

    public int getNumeroEventos() {
        return eventos.size();
    }

    public Evento getEvento(int posicao) {
        return eventos.get(posicao);
    }

    public void remove(int posicao) {
        eventos.remove(posicao);
    }
}
